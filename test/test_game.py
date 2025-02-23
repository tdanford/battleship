from typing import Optional
from battleship.messages import *
from battleship.game import Game, GameState


def test_game_setup_completed_state():
    p1 = RecordingMessageTarget(NullMessageTarget())
    p2 = RecordingMessageTarget(NullMessageTarget())
    g = Game("p1", p1, "p2", p2)
    g.deliver_message(Message("game", "setup"))

    assert g.state == GameState.SETUP
    assert p1.all_messages() == [Message("game", "setup")]
    assert p2.all_messages() == [Message("game", "setup")]

    g.deliver_message(Message("p1", "setup_complete"))
    g.deliver_message(Message("p2", "setup_complete"))
    assert g.state == GameState.PLAYING
    assert p1.last_message() == Message("game", "turn")
