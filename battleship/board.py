from typing import List, Dict, Optional, Tuple, Callable, Generator
from enum import Enum
import re
import random

from .ships import *

ROWS = 10
COLS = 10


class PlacedShip:

    ship: Ship
    start_coord: str
    orientation: Orientation
    locations: List["Spot"]

    def __init__(
        self, ship: Ship, board: "Board", start: str, orientation: Orientation
    ):
        self.ship = ship
        self.start_coord = start
        self.orientation = orientation
        pts = coord_range(self.ship.size(), start, orientation)
        self.locations = [board[format_coord(*c)] for c in pts]
        for l in self.locations:
            l.ship = self

    def unshot_spots(self) -> List["Spot"]:
        return [loc for loc in self.locations if not loc.is_shot]

    def overlaps(self, placed: "PlacedShip") -> bool:
        for spot in self.locations:
            if spot in placed.locations:
                return True
        return False

    @property
    def is_destroyed(self) -> bool:
        return len(self.unshot_spots()) == 0


class Spot:

    row: int
    col: int
    ship: Optional[PlacedShip]
    is_shot: bool

    def __init__(self, row: int, col: int):
        self.row = row
        self.col = col
        self.ship = None
        self.is_shot = False

    def count_remaining(self) -> int:
        if self.ship is not None:
            return len(self.ship.unshot_spots())
        else:
            return 0

    def shot_char(self) -> str:
        if self.is_shot:
            if self.ship is not None:
                return "X"
            else:
                return "o"
        else:
            return " "

    def ship_char(self) -> str:
        if self.ship is not None:
            if self.is_shot:
                return self.ship.ship.symbol().lower()
            else:
                return self.ship.ship.symbol().upper()
        else:
            return " "

    def shoot(self):
        self.is_shot = True

    @property
    def coord(self) -> str:
        return format_coord(self.row, self.col)

    def __repr__(self) -> str:
        return f"{self.coord}"

    def __eq__(self, other) -> bool:
        return (
            isinstance(other, Spot) and self.row == other.row and self.col == other.col
        )

    def __hash__(self) -> int:
        return hash((self.row, self.col))


coord_re = re.compile("([A-J])(\\d{1,2})")


def parse_coord(coord: str) -> Tuple[int, int]:
    m = coord_re.match(coord)
    if not m:
        raise ValueError(f"Can't parse {coord} as a coordinate")
    (first, second) = m.groups()
    row = ord(first) - ord("A")
    col = int(second) - 1
    return (row, col)


def format_row(row: int) -> str:
    return chr(ord("A") + row)


def format_coord(row: int, col: int) -> str:
    row_str = format_row(row)
    col_str = f"{col + 1}"
    return f"{row_str}{col_str}"


def rand_coord(max_row: int, max_col: int) -> str:
    rr = random.randint(0, max_row - 1)
    cc = random.randint(0, max_col - 1)
    return format_coord(rr, cc)


def coord_range(
    width: int, start_coord: str, orientation: Orientation
) -> List[Tuple[int, int]]:
    (start_row, start_col) = parse_coord(start_coord)
    if orientation == Orientation.HORIZONTAL:
        return [(start_row, start_col + i) for i in range(width)]
    else:
        return [(start_row + i, start_col) for i in range(width)]


def random_start_spot(width: int) -> Tuple[str, Orientation]:
    orient = random.choice([Orientation.HORIZONTAL, Orientation.VERTICAL])
    if orient == Orientation.VERTICAL:
        return (rand_coord(ROWS - width, COLS), orient)
    else:
        return (rand_coord(ROWS, COLS - width), orient)


direction_deltas = {"up": (-1, 0), "down": (1, 0), "left": (0, -1), "right": (0, 1)}


class Direction(Enum):

    UP = "up"
    DOWN = "down"
    LEFT = "left"
    RIGHT = "right"

    def delta(self) -> Tuple[int, int]:
        return direction_deltas.get(self.value)


class Board:

    spots: List[List[Spot]]
    placed_ships: List[PlacedShip]

    def __init__(self):
        self.spots = [[Spot(i, j) for j in range(COLS)] for i in range(ROWS)]
        self.placed_ships = []

    def shot_row(self, row: int) -> str:
        cs = [s.shot_char() for s in self.spots[row]]
        return "".join(cs)

    def ship_row(self, row: int) -> str:
        cs = [s.ship_char() for s in self.spots[row]]
        return "".join(cs)

    def render_both_boards(self) -> str:
        shots = self.render_shots()
        ships = self.render_ships()

        shot_lines = shots.split("\n")
        ship_lines = ships.split("\n")

        lines = [shot_lines[i] + "  " + ship_lines[i] for i in range(len(shot_lines))]
        return "\n".join(lines)

    def render_shots(self) -> str:
        bar = "".join(["-" for i in range(COLS)])
        col_ids = "".join([str((i + 1) % 10) for i in range(COLS)])
        sep = f" +{bar}+"
        top = f"  {col_ids}"
        board_lines = [self.shot_row(i) for i in range(ROWS)]
        lines = [
            top,
            sep,
            *[f"{format_row(i)}|{board_lines[i]}|" for i in range(len(board_lines))],
            sep,
        ]
        return "\n".join(lines)

    def render_ships(self) -> str:
        bar = "".join(["-" for i in range(COLS)])
        col_ids = "".join([str((i + 1) % 10) for i in range(COLS)])
        sep = f" +{bar}+"
        top = f"  {col_ids}"
        board_lines = [self.ship_row(i) for i in range(ROWS)]

        lines = [
            top,
            sep,
            *[f"{format_row(i)}|{board_lines[i]}|" for i in range(len(board_lines))],
            sep,
        ]
        return "\n".join(lines)

    def vertical_room(self, coord: str) -> int:
        return self.find_space(coord, Direction.UP) + self.find_space(
            coord, Direction.DOWN
        )

    def horizontal_room(self, coord: str) -> int:
        return self.find_space(coord, Direction.LEFT) + self.find_space(
            coord, Direction.RIGHT
        )

    def list_space_coords(
        self, coord: str, dir: Direction, max_width: int
    ) -> List[str]:
        (r, c) = parse_coord(coord)
        (rdelta, cdelta) = dir.delta()
        r += rdelta
        c += cdelta
        spaces = []
        # space < 4 because 4 + 1 is the maximum ship length, and we never care about
        # space that a ship occupies that _isn't_ in this space.
        while (
            r >= 0
            and c >= 0
            and r < ROWS
            and c < COLS
            and len(spaces) < max_width
            and not self.spots[r][c].is_shot
        ):
            spaces.append(format_coord(r, c))
            r += rdelta
            c += cdelta
        return spaces

    def find_space(self, coord: str, dir: Direction) -> int:
        (r, c) = parse_coord(coord)
        (rdelta, cdelta) = dir.delta()
        r += rdelta
        c += cdelta
        space = 0
        # space < 4 because 4 + 1 is the maximum ship length, and we never care about
        # space that a ship occupies that _isn't_ in this space.
        while (
            r >= 0
            and c >= 0
            and r < ROWS
            and c < COLS
            and space < 4
            and not self.spots[r][c].is_shot
        ):
            r += rdelta
            c += cdelta
            space += 1
        return space

    def has_alive_ships(self) -> bool:
        for p in self.placed_ships:
            if not p.is_destroyed:
                return True
        return False

    def place_ship(self, placed_ship: PlacedShip) -> bool:
        for s in self.placed_ships:
            if s.overlaps(placed_ship):
                return False

        self.placed_ships.append(placed_ship)
        return True

    def randomly_place_ship(self, ship: Ship) -> PlacedShip:
        is_legal = False
        while not is_legal:
            (coord, orient) = random_start_spot(ship.size())
            place = PlacedShip(ship, self, coord, orient)
            is_legal = self.place_ship(place)
        return place

    def __getitem__(self, coord: str) -> Spot:
        (row, col) = parse_coord(coord)
        assert row >= 0, f"Row {row} must be non-negative"
        assert col >= 0, f"Row {col} must be non-negative"
        assert row < ROWS, f"Row {row} must < {ROWS}"
        assert col < COLS, f"Row {col} must < {COLS}"
        return self.spots[row][col]

    def search_spots(
        self, predicate: Callable[[Spot], bool]
    ) -> Generator[Spot, None, None]:
        for i in range(ROWS):
            for j in range(COLS):
                if predicate(self.spots[i][j]):
                    yield self.spots[i][j]

    def unshot_spots(self) -> List[Spot]:
        return list(self.search_spots(lambda sp: not sp.is_shot))

    def shoot(self, coord: str) -> Tuple[bool, Optional[PlacedShip]]:
        spot = self[coord]
        first_remaining = spot.count_remaining()
        hit = spot.ship is not None
        spot.shoot()
        second_remaining = spot.count_remaining()
        if first_remaining > 0 and second_remaining == 0:
            # we've sunk a ship
            return (hit, spot.ship)
        else:
            return (hit, None)
