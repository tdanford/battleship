# A Battleship Game for Playing Against Kids

## Building

Building from source should be simple with the Gradle wrapper

```bash
./gradlew :{clean,build}
```

This should work from the top-level directory ('battleship') of the repository.

## Running the Game

Building the repository produces a packaged (all-in-one 'shadow') JAR, whose main class (`tdanford.battleship.Main`) is listed correctly in the JAR manifest.  Running it will drop you into a simple prompt-based text interface.  

```bash 
% java -jar build/libs/battleship-0.3-all.jar

Shot?
> a3
Human Player: A3 -> MISS 
Computer: B4 -> ?
	(enter: 'hit' or 'miss')
> miss
Computer Player: B4 -> MISS 
Shot?
> a5
Human Player: A5 -> MISS 
Computer: J2 -> ?
	(enter: 'hit' or 'miss')
> hit
```

## Writing Your Own Computer Player 

The default computer player, appropriately titled `DumbComputerPlayer`, simply shoots randomly around the board.  

Its code is simple, all it does is extend a base class `AutomatedPlayer` and define one method called `chooseAction`.  

```java
@Override
public BattleshipAction chooseAction(final BattleshipState publicKnowledge) {
  final BattleshipState.PlayerState currentState = publicKnowledge.getPlayerState(this);

  final Board board = currentState.shots;

  Spot shot = null;

  do {
    shot = randomShot(board);
  } while(!board.isNoShot(shot));

  return new BattleshipAction(this, shot);
}
```

All it does is choose a random location (where it hasn't shot before), and then package up the choice
in a helper class called `BattleshipAction`.  

It should be reasonably easy to re-define this method with some better logic; I'll be adding the ability to plug in arbitrary definitions for the computer player in the future.
