import click
import json
from pathlib import Path

from battleship.player import PlayerState


@click.group()
def main(): ...


@main.command("show")
@click.argument("state_file")
def show_state(state_file):
    p = Path(state_file)
    d = json.loads(p.read_text())
    ps = PlayerState.fromdict(d)
    click.echo(ps.render_both_boards())


if __name__ == "__main__":
    main()
