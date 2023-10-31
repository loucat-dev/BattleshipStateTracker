# Battleship State Tracker

## Background
This exercise is based on the classic game Battleship  
https://en.wikipedia.org/wiki/Battleship_(game)
- Two players
- Each have a 10x10 board
- During setup, players can place an arbitrary number of “battleships” on their board.
The ships are 1-by-n sized, must fit entirely on the board, and must be aligned either
vertically or horizontally.
- During play, players take turn “attacking” a single position on the opponent’s board,
and the opponent must respond by either reporting a “hit” on one of their battleships
(if one occupies that position) or a “miss”
- A battleship is sunk if it has been hit on all the squares it occupies
- A player wins if all of their opponent’s battleships have been sunk.

## The task
The task is to implement a Battleship state-tracker for a single player that must support the following logic:
- Create a board
- Add a battleship to the board
- Take an “attack” at a given position, and report back whether the attack resulted in a hit or a miss
- Return whether the player has lost the game yet (i.e. all battleships are sunk)

Application should not implement the entire game, just the state tracker. No UI or
persistence layer is required.

## Installation

The project needs Java 17 and Maven 3. \
After checkout, the tests can be run on the IDE of your choice or via command-line (if the above tools are installed), with this command:

`mvn clean install`

## Assumptions

- The 5 ships available are the '1990 Milton Bradley version' ones, as described in the wiki page.
- It is the single player that adds the battleship and chooses how to position the ships (as opposed to the computer randomly selecting the positions)
- The "attack" is coming from the computer (or from another player), and it just reports hit/miss, it does not say if a ship has sunk.
- I assume no type of UI is required, not even command-line, so unit and integration tests should be enough. 

## Improvements

- If the ability to play via command-line was required, the error handling should change to accommodate retrying after a validation error.
- The BoardGame is having too much responsibility and does not belong to the domain package anymore. Ideally I would use BoardGame to just represent the game itself, with its board/ships/rules, and would use another class to represent a game session, an ActiveGame class that uses that BoardGame, has its score and ship placements.
- The navigation of the board could be improved, maybe using a Custom Iterator.
- BattleshipService has a lot of validation, it might be better to move this logic to a Validator class.
- There are two ways to add a ship on board, with a single placement using `addShipOnBoard` or sending multiple or all placement via `addBattleshipOnBoard`. I have started developing the first method using TDD and that's why it is a public method, but if it's never going to be used outside the second method, it should be changed to private.
- To receive an attack I am assuming someone will choose the attack position, but as here the single player is the one receiving the attack, probably the position should be randomly generated to simulate a computer.
- Extra checks could be useful in a real-game scenario, for example making sure you cannot do any action if the game is over, or cannot start receiving attacks before all the 5 ships have been placed.
