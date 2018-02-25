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
