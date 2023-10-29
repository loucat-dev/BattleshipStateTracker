package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;

import java.util.HashMap;
import java.util.Map;

public class BoardGame {

    public static final Integer BOARD_SIZE = 10;
    public static final Integer MAX_SCORE = 17;
    public static final Integer LOWERCASE_A_ASCII_VALUE = 97;

    private Map<String, TargetSquare> board;

    private Map<Ship, Boolean> shipsOnBoard;

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
        this.score = 17;
        this.hasLost = false;
    }

    public Map<Ship, Boolean> getShipsOnBoard() {
        return shipsOnBoard;
    }

    public Map<String, TargetSquare> getBoard() {
        return board;
    }

    public void addShipOnBoard(Ship ship) {
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

    public Map<String, TargetSquare> populateEmptyBoard(){
        Map<String, TargetSquare> board = new HashMap<>();

        for(int position1 = 1; position1 <= BOARD_SIZE ; position1++) {
            for(int position2 = LOWERCASE_A_ASCII_VALUE; position2 <= BOARD_SIZE - 1 + LOWERCASE_A_ASCII_VALUE; position2++) {
                String numericalPosition = String.valueOf(position1);
                String alphabeticalPosition = Character.toString((char) position2);
                board.put(numericalPosition +  alphabeticalPosition, new TargetSquare(false, false));
            }
        }

        return board;
    }
}
