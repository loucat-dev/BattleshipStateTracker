package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;

import java.util.HashMap;
import java.util.Map;

import static net.luisa.battleship.service.BattleshipService.*;
import static net.luisa.battleship.utils.BoardUtils.*;

public class BoardGame {

    public static final Integer BOARD_SIZE = 10;
    public static final Integer MAX_SCORE = 17;
    public static final Integer SHIP_MAX_LENGTH = 5;

    private final Map<String, TargetSquare> board;

    private final Map<Ship, Boolean> shipsOnBoard;

    private int score;

    boolean hasLost;

    public BoardGame() {
        this.board = new HashMap<>();
        this.shipsOnBoard = new HashMap<>();
        this.score = MAX_SCORE;
        this.hasLost = false;

        shipsOnBoard.put(new Ship("Carrier", 5), false);
        shipsOnBoard.put(new Ship("Battleship", 4), false);
        shipsOnBoard.put(new Ship("Cruiser", 3), false);
        shipsOnBoard.put(new Ship("Submarine", 3), false);
        shipsOnBoard.put(new Ship("Destroyer", 2), false);
    }

    public BoardGame(Map<Ship, Boolean> shipsOnBoard) {
        this.board = new HashMap<>();
        this.shipsOnBoard = shipsOnBoard;
        this.score = MAX_SCORE;
        this.hasLost = false;
    }

    public BoardGame(Map<String, TargetSquare> board, Map<Ship, Boolean> shipsOnBoard, int score, boolean hasLost) {
        this.board = board;
        this.shipsOnBoard = shipsOnBoard;
        this.score = score;
        this.hasLost = hasLost;
    }

    public Map<Ship, Boolean> getShipsOnBoard() {
        return shipsOnBoard;
    }

    public Map<String, TargetSquare> getBoard() {
        return board;
    }

    private void decreaseScore() { //TODO: to test somehow
        this.score --;
        if (score <= 0) {
            this.hasLost = true;
        }
    }

    public void useShip(Ship ship) {
        if (canShipBeAdded(ship)) {
            shipsOnBoard.put(ship, true);
        }
    }

    public boolean canShipBeAdded(Ship ship){
        if(shipsOnBoard.containsKey(ship)){
            if (shipsOnBoard.get(ship)){
                throw new ShipValidationException(String.format("%s has already been placed on board", ship));
            }
        } else {
            throw new ShipValidationException(String.format("%s is not available", ship));
        }
        return true;
    }

    public AttackResult receiveAttack(ShipPlacement shipAttack){
        int numericPosition = getNumericPosition(shipAttack.position());
        char alphabeticPosition = getAlphabeticPosition(shipAttack.position());
        boolean isAttackSuccessful = false;
        String[] hitPositions= new String[shipAttack.ship().shipLength()];

        if (shipAttack.direction().equals(Direction.VERTICAL)) {
            int step = 0;
            while (step < shipAttack.ship().shipLength()) {
                numericPosition += step;
                if (board.containsKey(String.valueOf(numericPosition) + alphabeticPosition) && board.get(String.valueOf(numericPosition) + alphabeticPosition).withShip()) {
                    isAttackSuccessful = scoreAttack(numericPosition, alphabeticPosition);
                    hitPositions[step] = String.valueOf(numericPosition) + alphabeticPosition;
                }
                step++;
            }
        }

        if (shipAttack.direction().equals(Direction.HORIZONTAL)) {
            int endLetterNum = lettersToNumbersMap.get(alphabeticPosition) + shipAttack.ship().shipLength() - 1;
            char lastLetter = lettersOrderedList[endLetterNum - 1];
            int step = 0;
            while (alphabeticPosition <= lastLetter) {
                if (board.containsKey(String.valueOf(numericPosition) + alphabeticPosition) && board.get(String.valueOf(numericPosition) + alphabeticPosition).withShip()) {
                    isAttackSuccessful = scoreAttack(numericPosition, alphabeticPosition);
                    hitPositions[step] = String.valueOf(numericPosition) + alphabeticPosition;
                }
                alphabeticPosition++;
                step++;
            }
        }
        return new AttackResult(isAttackSuccessful, hitPositions);
    }

    private boolean scoreAttack(int numericPosition, char alphabeticPosition) {
        board.put(String.valueOf(numericPosition) + alphabeticPosition, new TargetSquare(true, true));
        decreaseScore();
        return true;
    }

//TODO: add end to end test
}
