package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.domain.TargetSquare;
import net.luisa.battleship.exception.ShipValidationException;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class BattleshipService {

    private final BoardGame boardGame;

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction){
        // check position is correct
        // check don't exceed board
        // check ship does not overlap
        // update targetSquare

        if (isShipPlacementValid(ship, position, direction)){
            return emptyMap();
        }
        return null;
    }

    private boolean isShipPlacementValid(Ship ship, String position, Direction direction){
        if (ship == null || position == null || position.isEmpty() || direction == null) {
            throw new ShipValidationException("ship, position and direction cannot be null or empty");
        }

        boardGame.canShipBeAdded(ship);

        return true;
    }
}