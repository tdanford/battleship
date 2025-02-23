from battleship.board import Spot, ShotOutcome, Board


def test_spot_equality():

    spot = Spot(3, 4)
    assert spot.row == 3
    assert spot.col == 4

    spot2 = Spot(3, 4)
    assert spot == spot2

    spot2.shoot()
    assert spot != spot2
    assert spot2.is_shot
    assert spot2.shot_outcome == ShotOutcome.MISS

    spot.shoot()
    assert spot == spot2


def test_board_spots():

    b = Board()

    s1 = b["C3"]
    s2 = b["C3"]

    assert s1 == s2
