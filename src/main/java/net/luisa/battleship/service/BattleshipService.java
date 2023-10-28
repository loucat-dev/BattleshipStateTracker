package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.domain.TargetSquare;
import net.luisa.battleship.exception.ShipValidationException;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class BattleshipService {

    private final static List<Ship> ships = List.of(new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2));

    private List<Ship> shipsToPlaceOnBoard = ships;

    private BoardGame boardGame;

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction){
        // check position is correct
        // check don't exceed board
        // check ship does not overlap
        // check ship is available, using shipsToPlaceOnBoard
        // update targetSquare

        if (isShipToAddValid(ship, position, direction)){
            return emptyMap();
        }
        return null;
    }

    private boolean isShipToAddValid(Ship ship, String position, Direction direction){
        if (ship == null || position == null || position.isEmpty() || direction == null) {
            throw new ShipValidationException("ship, position and direction cannot be null or empty");
        }
        return true;
    }
}