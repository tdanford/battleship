from enum import Enum

ship_widths = {
    "destroyer": 2,
    "cruiser": 3,
    "submarine": 3,
    "battleship": 4,
    "carrier": 5,
}

ship_chars = {
    "destroyer": "D",
    "cruiser": "C",
    "submarine": "S",
    "battleship": "B",
    "carrier": "A",
}


class Ship(Enum):

    DESTROYER = "destroyer"
    CRUISER = "cruiser"
    SUBMARINE = "submarine"
    BATTLESHIP = "battleship"
    CARRIER = "carrier"

    def symbol(self) -> str:
        return ship_chars.get(self.value)

    def name(self) -> str:
        return self.value

    def size(self) -> int:
        assert self.value in ship_widths
        return ship_widths.get(self.value)


class Orientation(Enum):

    HORIZONTAL = "horizontal"
    VERTICAL = "vertical"
