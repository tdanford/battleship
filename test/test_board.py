from battleship.board import *


def test_board_creation():
    b = Board()
    assert isinstance(b["A1"], Spot)
    assert isinstance(b["J10"], Spot)


def test_random_start_spot():
    for ship in Ship:
        for i in range(100):
            (coord, orient) = random_start_spot(ship.size())
            (r, c) = parse_coord(coord)
            assert r < ROWS
            assert c < COLS
            assert r >= 0
            assert c >= 0

            b = Board()
            b.place_ship(PlacedShip(ship, b, coord, orient))


def test_board_place_ship():
    b = Board()
    placed = b.randomly_place_ship(Ship.BATTLESHIP)
    assert placed.ship == Ship.BATTLESHIP


def test_find_space():

    b = Board()
    b.shoot("A3")
    b.shoot("D3")

    assert b.find_space("C3", Direction.UP) == 1
    assert b.find_space("C3", Direction.DOWN) == 0
    assert b.find_space("C3", Direction.LEFT) == 2
    assert b.find_space("C3", Direction.RIGHT) == 4
    assert b.vertical_room("C3") == 1
    assert b.horizontal_room("C3") == 6

    b.shoot("C2")

    assert b.find_space("C3", Direction.UP) == 1
    assert b.find_space("C3", Direction.DOWN) == 0
    assert b.find_space("C3", Direction.LEFT) == 0
    assert b.find_space("C3", Direction.RIGHT) == 4
    assert b.vertical_room("C3") == 1
    assert b.horizontal_room("C3") == 4


def test_list_spaces():

    b = Board()
    b.shoot("A3")
    b.shoot("D3")

    b.list_space_coords("C3", Direction.UP, 5) == ["B3"]
    b.list_space_coords("C3", Direction.DOWN, 5) == []
    b.list_space_coords("C3", Direction.LEFT, 5) == ["C2", "C1"]
    b.list_space_coords("C3", Direction.RIGHT, 5) == ["C4", "C5", "C6", "C7"]

    b.shoot("C2")


def test_board_equality():
    b1 = Board()
    b2 = Board()
    assert b1 == b2

    b2.place_ship(PlacedShip(Ship.BATTLESHIP, b2, "C3", Orientation.HORIZONTAL))
    assert b1 != b2

    b1.place_ship(PlacedShip(Ship.BATTLESHIP, b1, "C3", Orientation.HORIZONTAL))
    assert b1 == b2

    b1.shoot("F5")
    assert b1 != b2

    b2.shoot("F5")
    assert b1 == b2


def test_board_unshot_spots(): 

    b1 = Board()

    assert "C1" in b1.unshot_spots()
    assert "C2" in b1.unshot_spots()
    assert "C3" in b1.unshot_spots()

    b1.shoot("C3") 

    assert "C1" in b1.unshot_spots()
    assert "C2" in b1.unshot_spots()
    assert "C3" not in b1.unshot_spots()

    b1.shoot("C2", ShotOutcome.MISS)

    assert "C1" in b1.unshot_spots()
    assert "C2" not in b1.unshot_spots()
    assert "C3" not in b1.unshot_spots()

    b1.shoot("C1", ShotOutcome.HIT)

    assert "C1" not in b1.unshot_spots()
    assert "C2" not in b1.unshot_spots()
    assert "C3" not in b1.unshot_spots()

    b1.place_ship(PlacedShip(Ship.BATTLESHIP, b1, "D1", Orientation.HORIZONTAL))

    assert "D1" in b1.unshot_spots()

    b1.shoot("D1")
    
    assert "D1" not in b1.unshot_spots()
