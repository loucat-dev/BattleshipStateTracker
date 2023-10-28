package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;

import java.util.HashMap;
import java.util.Map;

public class BoardGame {

    private Map<String, TargetSquare> board;

    private Map<Ship, Boolean> shipsOnBoard;

    private int score;

    boolean hasLost;

    public BoardGame(Map<String, TargetSquare> board) {
        this.board = board;
        this.shipsOnBoard = new HashMap<>();
        this.score = 17;
        this.hasLost = false;

        shipsOnBoard.put(new Ship("Carrier", 5), false);
        shipsOnBoard.put(new Ship("Battleship", 4), false);
        shipsOnBoard.put(new Ship("Cruiser", 3), false);
        shipsOnBoard.put(new Ship("Submarine", 3), false);
        shipsOnBoard.put(new Ship("Destroyer", 2), false);
    }


    public void addShipOnBoard(Ship ship){
        if(shipsOnBoard.containsKey(ship)){
            if (shipsOnBoard.get(ship)){
                throw new ShipValidationException(String.format("%s has already been placed on board", ship));
            }
            shipsOnBoard.put(ship, true);
        } else {
            throw new ShipValidationException(String.format("%s is not available", ship));
        }
    }
}
