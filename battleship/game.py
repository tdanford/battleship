from .messages import *
from .board import *
from .player import *
from .queues import MessageQueue

import asyncio
import json
import logging

from pathlib import Path


class GameState(Enum):

    SETUP = "setup"
    PLAYING = "playing"
    ENDED = "ended"

class Game(MessageTarget, ABC): 
    """A Game object is the master state of a running Battleship game.

    It can see the complete state of both (or, in a multiplayer future, all?) players, and
    it mediates communication by messages between the players.

    This is built like this to support both local and remote game-play, imperfect information
    for individual players, and asynchronous interactions between players or between players and the
    game state itself.
    """

    @abstractmethod
    def send_message(self, target: str, message: Message):
        ...

    def send(self, target: str, type: str, **kwargs):
        self.send_message(target, Message(source="game", type=type, payload={**kwargs}))
    
    @abstractmethod 
    def place_ship(self, player: str, ship: Ship, start_coord: str, orientation: Orientation): 
        ...

    @abstractmethod
    def switch_turn(self):
        ...
    
    @abstractmethod 
    def is_all_setup_complete(self): 
        ...
    
    @abstractmethod 
    def set_setup_complete(self, player: str): 
        ...
    
    @abstractmethod
    def player_names(self) -> List[str]: 
        ...

    @abstractmethod 
    def current_player(self) -> str: 
        ...
    
    @abstractmethod
    def other_player(self, player: str) -> str:
        ...
    
    @abstractmethod 
    def shoot_at_board(self, player: str, coord: str) -> Tuple[bool, Optional[PlacedShip]]:
        ...
    
    @abstractmethod
    def log(self, level, msg, *args): 
        ...
    
    @abstractmethod 
    def is_alive(self, player: str) -> bool: 
        ...
    
    @abstractmethod 
    def end_game(self, winner: str): 
        ...

    def deliver_message(self, message):
        if message.type == "setup":
            self.state = GameState.SETUP
            for p in self.player_names():
                self.send(p, "setup")
        elif message.type == "ship_placed":
            if self.state != GameState.SETUP:
                raise ValueError(
                    f"Illegal state {self.state} for {message.type} message"
                )
            ship = Ship(message.payload.get("ship"))
            start_coord = message.payload.get("coord")
            orientation = Orientation(message.payload.get("orientation"))
            self.place_ship(message.source, ship, start_coord, orientation)
        elif message.type == "setup_complete":
            if self.state != GameState.SETUP:
                raise ValueError(
                    f"Illegal state {self.state} for {message.type} message"
                )
            self.set_setup_complete(message.source)
            if self.is_all_setup_complete():
                self.state = GameState.PLAYING
                self.switch_turn()
        elif message.type == "shot":
            if self.state != GameState.PLAYING:
                raise ValueError(
                    f"Illegal state {self.state} for {message.type} message"
                )

            target_player = self.other_player(message.source)

            # update internal state of the target player
            coord = message.payload.get("coord")
            (hit, sunk_ship) = self.shoot_at_board(target_player, coord) 
            feedback_payload = {"coord": coord, "outcome": "hit" if hit else "miss"}
            if sunk_ship is not None:
                feedback_payload["sunk_ship"] = sunk_ship.ship.value

            # transmit move to other player
            self.send_message(target_player, message)

            # give the first player the feedback on the shot
            self.send(message.source, "shot_feedback", **feedback_payload)

            if self.is_alive(target_player): 
                self.switch_turn()
            else:
                self.end_game(message.source)
                self.send(target_player, "lost")
                self.send(message.source, "won")

class LocalGame(Game):

    state: GameState
    turn: int
    names: Tuple[str, str]
    states: Dict[str, PlayerState]
    players: Dict[str, MessageTarget]
    setup: Dict[str, bool]
    logger: logging.Logger

    def __init__(
        self, name1: str, player1: MessageTarget, name2: str, player2: MessageTarget
    ):
        self.logger = logging.getLogger("game")
        self.state = GameState.SETUP
        self.turn = 1
        self.names = (name1, name2)
        self.states = {name1: PlayerState(name1), name2: PlayerState(name2)}
        self.players = {name1: player1, name2: player2}
        self.setup = {name1: False, name2: False}

    def player_names(self) -> List[str]: 
        return list(self.names) 
    
    def current_player(self) -> str: 
        return self.names[self.turn]

    def other_player(self, player: str) -> str:
        if player == self.names[0]:
            return self.names[1]
        elif player == self.names[1]:
            return self.names[0]
        else:
            raise ValueError(player)
    
    def end_game(self, winner: str):
        self.state = GameState.ENDED
        self.log(
            logging.INFO, f"Game ended with a win for {winner}"
        )
    
    def is_alive(self, player: str) -> bool: 
        return self.states[player].is_alive()
    
    def log(self, level, msg, *args):
        self.logger.log(level, msg, *args)

    def send_message(self, target: str, message: Message):
        self.players.get(target).deliver_message(message)

    def place_ship(self, player: str, ship: Ship, start_coord: str, orientation: Orientation): 
        board = self.states.get(player).home_board
        board.place_ship(PlacedShip(ship, board, start_coord, orientation))

    def is_all_setup_complete(self) -> bool:
        return self.setup[self.names[0]] and self.setup[self.names[1]]

    def set_setup_complete(self, player: str): 
        self.setup[player] = True

    def switch_turn(self):
        """Flips the `turn` variable to index the next player to play, and sends a `turn` Message to that player"""
        self.turn = 1 - self.turn
        self.print_ship_boards()
        self.send(self.names[self.turn], "turn")

    def shoot_at_board(self, player: str, coord: str) -> Tuple[bool, Optional[PlacedShip]]:
        (hit, pship) = self.states.get(player).home_board.shoot(coord)
        self.states.get(self.other_player(player)).target_board.shoot(coord)
        return (hit, pship)

    def print_ship_boards(self):
        for name, state in self.states.items():
            print(name)
            print(state.render_both_boards())

    async def run(self):
        """The central loop of the game, which just checks regularly to see if the game is finished.

        All the actual logic updates happen in the `deliver_message` method.
        """
        self.deliver_message(Message(source="run", type="setup", payload={}))
        while self.state != GameState.ENDED:
            await asyncio.sleep(0.5)
        raise GameEndedException()


class GameEndedException(Exception):
    """Raise this to end the game from a queue runner or the game runner"""


async def setup_local_game() -> LocalGame:
    logging.basicConfig(level=logging.INFO)

    game_target = ProxyingMessageTarget()
    game_queue = MessageQueue("game", game_target)
    p2 = HuntingComputerPlayer("player1", game_queue)
    p1 = RandomComputerPlayer("player2", game_queue)
    game = LocalGame("player1", p1, "player2", p2)
    game_target.set_target(game)

    try:
        async with asyncio.TaskGroup() as group:
            group.create_task(game_queue.run())
            group.create_task(game.run())
    except* GameEndedException:
        pass

    return game
