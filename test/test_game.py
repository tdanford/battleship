from typing import Optional
from battleship.messages import *
from battleship.game import LocalGame, GameState


def test_game_setup_completed_state():
    p1 = RecordingMessageTarget(NullMessageTarget())
    p2 = RecordingMessageTarget(NullMessageTarget())
    g = LocalGame("p1", p1, "p2", p2)
    g.deliver_message(Message("game", "setup"))

    assert g.state == GameState.SETUP
    assert p1.all_messages() == [Message("game", "setup")]
    assert p2.all_messages() == [Message("game", "setup")]

    g.deliver_message(Message("p1", "setup_complete"))
    g.deliver_message(Message("p2", "setup_complete"))
    assert g.state == GameState.PLAYING
    assert p1.last_message() == Message("game", "turn")

def test_game_other_player(): 
    p1 = RecordingMessageTarget(NullMessageTarget())
    p2 = RecordingMessageTarget(NullMessageTarget())
    g = LocalGame("p1", p1, "p2", p2)

    assert g.other_player("p1") == "p2"
    assert g.other_player("p2") == "p1"

def test_game_turn():
    p1 = RecordingMessageTarget(NullMessageTarget())
    p2 = RecordingMessageTarget(NullMessageTarget())
    g = LocalGame("p1", p1, "p2", p2)
    g.deliver_message(Message("game", "setup"))

    # First step: put the game into setup mode
    assert g.state == GameState.SETUP
    assert p1.all_messages() == [Message("game", "setup")]
    assert p2.all_messages() == [Message("game", "setup")]

    # P2 places one ship, at B1, going right 
    g.deliver_message(Message("p2", "ship_placed", dict(ship="battleship", coord="B1", orientation="horizontal")))

    # Signal that setup is complete; now the game should be PLAYING 
    g.deliver_message(Message("p1", "setup_complete"))
    g.deliver_message(Message("p2", "setup_complete"))
    assert g.state == GameState.PLAYING
    assert p1.last_message() == Message("game", "turn")

    # P1 makes the first move, a shot at B2
    g.deliver_message(Message("p1", "shot", dict(coord="B2")))
    # P2 should get two messages, a message about the P1 shot at B2 and a message 
    # that it's now P2's turn
    assert p2.last_message(-2) == Message("p1", "shot", dict(coord="B2"))
    assert p2.last_message() == Message("game", "turn")
    # P1 on the other hand gets a shot_feedback message, that B2 was a hit 
    assert p1.last_message() == Message("game", "shot_feedback", dict(coord="B2", outcome="hit"))

    # Now we can assert things about the home/target boards for each player's state 
    # within the game.
    assert g.states["p1"].target_board["B2"].is_shot
    assert not g.states["p1"].target_board["B1"].is_shot
    assert not g.states["p1"].home_board["B2"].is_shot

    assert g.states["p2"].home_board["B2"].is_shot
    assert not g.states["p2"].home_board["B1"].is_shot
    assert not g.states["p2"].target_board["B2"].is_shot

    assert "B2" not in g.states["p1"].target_board.unshot_spots()

    print(g.states["p2"].target_board.unshot_spots())
    assert "B2" in g.states["p2"].target_board.unshot_spots()
