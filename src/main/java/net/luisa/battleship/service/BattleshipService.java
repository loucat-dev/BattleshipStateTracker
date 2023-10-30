package net.luisa.battleship.service;

import net.luisa.battleship.domain.*;
import net.luisa.battleship.exception.ShipValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.luisa.battleship.domain.BoardGame.BOARD_SIZE;
import static net.luisa.battleship.domain.BoardGame.SHIP_MAX_LENGTH;
import static net.luisa.battleship.utils.BoardUtils.*;

public class BattleshipService {

    private final BoardGame boardGame;

    private static final Logger log = LoggerFactory.getLogger(BattleshipService.class);

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction) {

        String[] positionsToOccupy = new String[SHIP_MAX_LENGTH];
        Map<String, TargetSquare> board = boardGame.getBoard();

        if (isShipPlacementValid(ship, position, direction, positionsToOccupy, board)) {
            boardGame.useShip(ship);

            for (String positionToOccupy : positionsToOccupy) {
                board.put(positionToOccupy, new TargetSquare(true, false));
            }
        }
        return board;
    }

    public Map<String, TargetSquare> addBattleshipOnBoard(List<ShipPlacement> shipPlacements) {
        return shipPlacements.stream()
                .map(shipPlacement -> addShipOnBoard(shipPlacement.ship(), shipPlacement.position(), shipPlacement.direction()))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private boolean isShipPlacementValid(Ship ship, String position, Direction direction, String[] positionsToOccupy, Map<String, TargetSquare> board) {
        if (ship == null || position == null || position.isEmpty() || direction == null) {
            throw new ShipValidationException("ship, position and direction cannot be null or empty");
        }

        boardGame.canShipBeAdded(ship);

        if (!isPositionValid(position)) {
            throw new ShipValidationException(String.format("position %s is not valid", position));
        }

        if (!isPlacementWithinBorders(ship, position, direction)) {
            throw new ShipValidationException(String.format("ship %s in position %s exceeds board", ship, position));
        }

        if (isPositionOccupied(ship, position, direction, board, positionsToOccupy)) {
            throw new ShipValidationException(String.format("ship %s in position %s overlaps with existing ships", ship, position));
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
            if (alphabeticPosition > 'j') {
                log.error("position '{}' needs a second character between a-j", position);
                return false;
            }
        } catch (NumberFormatException nfe) {
            log.error("position '{}' needs a first numeric character between 1-10", position);
            return false;
        }
        return true;
    }

    private boolean isPlacementWithinBorders(Ship ship, String position, Direction direction) {
        int numericPosition = getNumericPosition(position);
        if (direction.equals(Direction.VERTICAL)) {
            if (numericPosition + ship.shipLength() - 1 > BOARD_SIZE) {
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }

        char alphabeticPosition = getAlphabeticPosition(position);

        if (direction.equals(Direction.HORIZONTAL)) {
            if (lettersToNumbersMap.get(alphabeticPosition) + ship.shipLength() - 1 > BOARD_SIZE) {
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }
        return true;
    }

    private boolean isPositionOccupied(Ship ship, String position, Direction direction, Map<String, TargetSquare> board, String[] positionsToOccupy) {
        int numericPosition = getNumericPosition(position);
        char alphabeticPosition = getAlphabeticPosition(position);

        if (direction.equals(Direction.VERTICAL)) {
            int step = 0;
            while (step < ship.shipLength()) {
                numericPosition += step;
                if (board.containsKey(String.valueOf(numericPosition) + alphabeticPosition) && board.get(String.valueOf(numericPosition) + alphabeticPosition).withShip()) {
                    return true;
                }
                positionsToOccupy[step] = String.valueOf(numericPosition) + alphabeticPosition;
                step++;
            }
        }

        if (direction.equals(Direction.HORIZONTAL)) {
            int step = 0;
            int endLetterNum = lettersToNumbersMap.get(alphabeticPosition) + ship.shipLength() - 1;
            char lastLetter = lettersOrderedList[endLetterNum - 1];
            while (alphabeticPosition <= lastLetter) {
                if (board.containsKey(String.valueOf(numericPosition) + alphabeticPosition) && board.get(String.valueOf(numericPosition) + alphabeticPosition).withShip()) {
                    return true;
                }
                positionsToOccupy[step] = String.valueOf(numericPosition) + alphabeticPosition;
                alphabeticPosition++;
                step++;
            }
        }
        return false;
    }
}