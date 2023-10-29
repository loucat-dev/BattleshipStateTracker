package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.domain.TargetSquare;
import net.luisa.battleship.exception.ShipValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Collections.emptyMap;

public class BattleshipService {

    private final BoardGame boardGame;

    private static final Logger log = LoggerFactory.getLogger(BattleshipService.class);

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction) {
        // check don't exceed board
        // check ship does not overlap
        // update targetSquare

        if (isShipPlacementValid(ship, position, direction)) {
            return emptyMap();
        }
        return null;
    }

    private boolean isShipPlacementValid(Ship ship, String position, Direction direction) {
        if (ship == null || position == null || position.isEmpty() || direction == null) {
            throw new ShipValidationException("ship, position and direction cannot be null or empty");
        }

        boardGame.canShipBeAdded(ship);

        if(!isPositionValid(position)){
            throw new ShipValidationException(String.format("position %s is not valid", position));
        }

        return true;
    }

    private boolean isPositionValid(String position) {
        if (position.length() < 2 || position.length() > 3) {
            log.error("position '{}' must have length 2 or 3", position);
            return false;
        }

        try {
            int numericPosition = Integer.parseInt(position.substring(0, position.length() - 1 ));
            char alphabeticPosition = (position.substring(position.length() - 2,position.length() - 1).toLowerCase().charAt(0));

            if (numericPosition < 1 || numericPosition > 10) {
                log.error("position '{}' needs a first numeric character between 1-10", position);
                return false;
            }
            if( alphabeticPosition <= 'j'){
                log.error("position '{}' needs a second character between a-j", position);
                return false;
            }
        } catch (NumberFormatException nfe) {
            log.error("position '{}' needs a first numeric character between 1-10", position);
            return false;
        }
        return true;
    }
}