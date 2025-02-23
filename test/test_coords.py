
from battleship.board import * 

def test_format_coord(): 
    assert format_coord(0, 0) == 'A1'
    assert format_coord(1, 1) == 'B2'

def test_parse_coord(): 
    assert parse_coord('A1') == (0, 0)
    assert parse_coord('B2') == (1, 1)
    assert parse_coord('J10') == (9, 9)

def test_rand_coord(): 
    for i in range(100): 
        (r, c) = parse_coord(rand_coord(10, 10)) 
        assert r >= 0 
        assert c >= 0 
        assert r < 10 
        assert c < 10



