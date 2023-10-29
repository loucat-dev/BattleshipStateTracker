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
import static net.luisa.battleship.domain.BoardGame.BOARD_SIZE;

public class BattleshipService {

    private final BoardGame boardGame;

    private static final Logger log = LoggerFactory.getLogger(BattleshipService.class);

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction) {
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

        if(!isPlacementWithinBorders(ship, position, direction)){
            throw new ShipValidationException(String.format("ship %s in position %s exceeds board", ship, position));
        }


        return true;
    }

    private boolean isPositionValid(String position) {
        if (position.length() < 2 || position.length() > 3) {
            log.error("position '{}' must have length 2 or 3", position);
            return false;
        }

        try {
            int numericPosition = getNumericPosition(position);
            char alphabeticPosition = getAlphabeticPosition(position);

            if (numericPosition < 1 || numericPosition > BOARD_SIZE) {
                log.error("position '{}' needs a first numeric character between 1-10", position);
                return false;
            }
            if (alphabeticPosition > 'j'){
                log.error("position '{}' needs a second character between a-j", position);
                return false;
            }
        } catch (NumberFormatException nfe) {
            log.error("position '{}' needs a first numeric character between 1-10", position);
            return false;
        }
        return true;
    }

    private static char getAlphabeticPosition(String position) {
        return position.substring(position.length() - 1).toLowerCase().charAt(0);
    }

    private static int getNumericPosition(String position) {
        return Integer.parseInt(position.substring(0, position.length() - 1));
    }

    private boolean isPlacementWithinBorders(Ship ship, String position, Direction direction) {
        int numericPosition = getNumericPosition(position);
        if (direction.equals(Direction.VERTICAL)){
            if (numericPosition + ship.shipLength() - 1 > BOARD_SIZE){
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }

        char alphabeticPosition = getAlphabeticPosition(position);
        Map<Character, Integer> alphabeticPositionsMap =  Map.of('a', 1, 'b', 2, 'c', 3, 'd', 4, 'e', 5,
                'f', 6, 'g', 7, 'h', 8, 'i', 9, 'j', 10);
        if (direction.equals(Direction.HORIZONTAL)){
            if (alphabeticPositionsMap.get(alphabeticPosition) + ship.shipLength() - 1 > BOARD_SIZE) {
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }

        return true;
    }
}