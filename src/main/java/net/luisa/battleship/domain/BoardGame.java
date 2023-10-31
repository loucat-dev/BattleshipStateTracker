package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;

import java.util.HashMap;
import java.util.Map;

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

    public boolean hasLost() {
        return hasLost;
    }

    private void decreaseScore() {
        this.score--;
        if (score <= 0) {
            this.hasLost = true;
        }
    }

    public void useShip(Ship ship) {
        if (canShipBeAdded(ship)) {
            shipsOnBoard.put(ship, true);
        }
    }

    public boolean canShipBeAdded(Ship ship) {
        if (shipsOnBoard.containsKey(ship) && shipsOnBoard.get(ship)) {
                throw new ShipValidationException(String.format("%s has already been placed on board", ship));
        }
        return true;
    }

    public AttackResult receiveAttack(String position) {
        boolean isAttackSuccessful = false;
        if (board.containsKey(position) && board.get(position).withShip() && !board.get(position).isHit()) {
            board.put(position, new TargetSquare(true, true));
            isAttackSuccessful = true;
            decreaseScore();
        }
        return new AttackResult(isAttackSuccessful, score);
    }
}
