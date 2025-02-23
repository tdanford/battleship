import random
import logging
from abc import abstractmethod, ABC
from typing import TypeVar

from .board import *
from .messages import *


class PlayerState(MessageTarget):

    name: str
    home_board: Board
    target_board: Board
    logger: logging.Logger

    def __init__(self, name: str):
        self.name = name
        self.home_board = Board()
        self.target_board = Board()
        self.logger = logging.getLogger(self.name)

    def deliver_message(self, message):
        if message.type == "shot":
            coord = message.payload.get("coord")
            (hit, ship) = self.home_board.shoot(coord)
            if hit:
                self.logger.log(logging.INFO, f"Hit ship")
                if ship is not None:
                    self.logger.log(logging.INFO, f"Sunk {ship.ship}")

    def is_alive(self) -> bool:
        return self.home_board.has_alive_ships()


class Player(PlayerState):

    game: MessageTarget
    outcome: str

    def __init__(self, name: str, game: MessageTarget):
        super().__init__(name)
        self.game = game
        self.outcome = "playing"

    def send(self, type: str, **kwargs):
        self.game.deliver_message(
            Message(source=self.name, type=type, payload={**kwargs})
        )

    def deliver_message(self, message: Message):
        PlayerState.deliver_message(self, message)

        if message.type == "setup":
            self.setup()
        elif message.type == "setup_complete":
            for placed in self.home_board.placed_ships:
                self.send(
                    "ship_placed",
                    ship=placed.ship.name(),
                    coord=placed.start_coord,
                    orientation=placed.orientation.value,
                )
            self.send("setup_complete")
        elif message.type == "turn":
            coord = self.choose_shot()
            self.send("shot", coord=coord)
        elif message.type == "shot_feedback":
            coord = message.payload.get("coord")
            outcome = message.payload.get("outcome")
            sunk_ship = message.payload.get("sunk_ship")
            self.shot_outcome(coord, outcome, sunk_ship)
        elif message.type == "won":
            self.outcome = message.type
            self.set_outcome(self.outcome)
        elif message.type == "lost":
            self.outcome = message.type
            self.set_outcome(self.outcome)

    @abstractmethod
    def setup(self): ...

    @abstractmethod
    def choose_shot(self) -> str: ...

    @abstractmethod
    def shot_outcome(self, coord: str, outcome: str, sunk_ship: Optional[str]): ...

    @abstractmethod
    def set_outcome(self, outcome: str): ...


class RandomComputerPlayer(Player):

    def __init__(self, name: str, game: MessageTarget):
        super().__init__(name, game)

    def setup(self):
        self.randomly_place_ships()
        self.deliver_message(
            Message(source=self.name, type="setup_complete", payload={})
        )

    def choose_shot(self):
        return self.choose_random_shot()

    def set_outcome(self, outcome):
        self.logger.log(logging.INFO, f"Game outcome: {outcome}")

    def shot_outcome(self, coord, outcome, sunk_ship):
        self.logger.log(logging.INFO, f"Shot at {coord} was a {outcome}")
        if sunk_ship is not None:
            self.logger.log(logging.INFO, f"We sank a {sunk_ship}")

    def randomly_place_ships(self):
        for ship in Ship:
            self.home_board.randomly_place_ship(ship)

    def choose_random_shot(self) -> str:
        spots = self.target_board.unshot_spots()
        random_spot = random.choice(spots)
        return random_spot.coord


class HuntingState(Enum):

    SEARCHING = "searching"
    HUNTING = "hunting"


T = TypeVar("T")


def choose_by_weight(values: List[T], weights: List[int]) -> T:
    total = sum(weights)
    fs = [w / total for w in weights]
    p = random.random()
    for i in range(len(fs)):
        p -= fs[i]
        if p < 0.0:
            return values[i]
    return values[-1]


class HuntingComputerPlayer(Player):

    state: HuntingState
    remaining_ships: Dict[Ship, bool]
    pending: Dict[Direction, List[str]]
    pending_directions: List[Direction]

    def __init__(self, name: str, game: MessageTarget):
        super().__init__(name, game)
        self.remaining_ships = {s: True for s in Ship}
        self.state = HuntingState.SEARCHING
        self.pending = {}

    def count_ship_freedoms(self, coord: str, ship: Ship) -> int:
        vr = self.target_board.vertical_room(coord) + 1 - ship.size()
        hr = self.target_board.horizontal_room(coord) + 1 - ship.size()
        verts = max(0, vr)
        horizs = max(0, hr)
        return verts + horizs

    def count_freedoms(self, coord: str) -> int:
        fs = [
            self.count_ship_freedoms(coord, s) if self.remaining_ships[s] else 0
            for s in self.remaining_ships
        ]
        return sum(fs)

    def setup(self):
        self.randomly_place_ships()
        self.deliver_message(
            Message(source=self.name, type="setup_complete", payload={})
        )

    def choose_shot(self):
        if self.state == HuntingState.SEARCHING:
            spots = [
                format_coord(s.row, s.col) for s in self.target_board.unshot_spots()
            ]
            self.logger.log(logging.INFO, f"Spots: {spots}")
            freedoms = [self.count_freedoms(c) for c in spots]
            self.logger.log(logging.INFO, f"Freedoms: {freedoms}")
            next_spot = choose_by_weight(spots, freedoms)
            self.logger.log(logging.INFO, f"Chose {next_spot}")
            return next_spot
        else:
            self.logger.log(logging.INFO, f"Pending {self.pending}")
            while len(self.pending[self.pending_directions[0]]) == 0:
                self.pending_directions.pop(0)
            return self.pending[self.pending_directions[0]].pop(0)

    def set_outcome(self, outcome):
        self.logger.log(logging.INFO, f"Game outcome: {outcome}")

    def shot_outcome(self, coord, outcome, sunk_ship):
        self.logger.log(logging.INFO, f"Shot at {coord} was a {outcome}")
        if self.state == HuntingState.SEARCHING:
            if outcome == "hit":
                # if we're searching, and we got a hit, then we load up
                # the pending and pending_directions, which are the spots we're
                # going to shoot out to sink this new ship.
                self.state = HuntingState.HUNTING
                self.logger.log(
                    logging.INFO, f"A {outcome} switchees us to {self.state}"
                )
                self.pending_directions = [dir for dir in Direction]
                max_width = max(
                    [s.size() for (s, v) in self.remaining_ships.items() if v]
                )
                self.pending = {
                    dir: self.target_board.list_space_coords(coord, dir, max_width - 1)
                    for dir in self.pending_directions
                }
        else:
            if outcome == "miss":
                # if we are hunting, and we get a miss, then we _stop_ shooting
                # in that direction.
                self.logger.log(
                    logging.INFO,
                    f"A miss while HUNTING clears the {self.pending_directions[0]} direction",
                )
                self.pending[self.pending_directions[0]] = []
                self.pending_directions.pop(0)

        if sunk_ship is not None:
            self.logger.log(logging.INFO, f"We sank a {sunk_ship}")
            # when we sink a ship, we go back to shooting randomly (aka 'searching')
            self.state = HuntingState.SEARCHING
            self.logger.log(
                logging.INFO, f"A sunk ship switches us back to {self.state}"
            )
            self.pending = {}

    def randomly_place_ships(self):
        for ship in Ship:
            self.home_board.randomly_place_ship(ship)

    def choose_random_shot(self) -> str:
        spots = self.target_board.unshot_spots()
        random_spot = random.choice(spots)
        return random_spot.coord
