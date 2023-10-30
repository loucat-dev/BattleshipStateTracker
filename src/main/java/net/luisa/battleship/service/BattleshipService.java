package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.domain.TargetSquare;
import net.luisa.battleship.exception.ShipValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static net.luisa.battleship.domain.BoardGame.BOARD_SIZE;

public class BattleshipService {

    private final BoardGame boardGame;

    private static final Logger log = LoggerFactory.getLogger(BattleshipService.class);

    private static final Map<Character, Integer> lettersToNumbersMap =  Map.of('a', 1, 'b', 2, 'c', 3, 'd', 4, 'e', 5,
            'f', 6, 'g', 7, 'h', 8, 'i', 9, 'j', 10);

    private static final char[] lettersOrderedList = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

    public BattleshipService(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Map<String, TargetSquare> addShipOnBoard(Ship ship, String position, Direction direction) {

        String[] positionsToOccupy = new String[ship.shipLength()];
        Map<String, TargetSquare> board = new HashMap<>();

        if (isShipPlacementValid(ship, position, direction, positionsToOccupy)) {
            boardGame.useShip(ship);

            board = boardGame.getBoard();

            for(String positionToOccupy: positionsToOccupy){
                board.put(positionToOccupy, new TargetSquare(true, false));
            }
        }
        return board;
    }

    private boolean isShipPlacementValid(Ship ship, String position, Direction direction, String[] positionsToOccupy) {
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

        Map<String, TargetSquare> board = boardGame.getBoard();

        if (isPositionOccupied(ship, position, direction, board, positionsToOccupy)){
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

    private boolean isPlacementWithinBorders(Ship ship, String position, Direction direction) {
        int numericPosition = getNumericPosition(position);
        if (direction.equals(Direction.VERTICAL)){
            if (numericPosition + ship.shipLength() - 1 > BOARD_SIZE){
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }

        char alphabeticPosition = getAlphabeticPosition(position);

        if (direction.equals(Direction.HORIZONTAL)){
            if (lettersToNumbersMap.get(alphabeticPosition) + ship.shipLength() - 1 > BOARD_SIZE) {
                log.error("{} in position {} exceeds board", ship, position);
                return false;
            }
        }
        return true;
    }

    private boolean isPositionOccupied(Ship ship, String position, Direction direction, Map<String, TargetSquare> board, String[] positionsToOccupy){
        int numericPosition = getNumericPosition(position);
        char alphabeticPosition = getAlphabeticPosition(position);

        if (direction.equals(Direction.VERTICAL)){
            int step = 0;
            while(step < ship.shipLength()){
                numericPosition += step;
                if(board.containsKey(String.valueOf(numericPosition) +  alphabeticPosition) && board.get(String.valueOf(numericPosition) +  alphabeticPosition).withShip()){
                    return true;
                }
                positionsToOccupy[step] = String.valueOf(numericPosition) +  alphabeticPosition;
                step++;
            }
        }

        if (direction.equals(Direction.HORIZONTAL)){
            int step = 0;
            int endLetterNum = lettersToNumbersMap.get(alphabeticPosition) + ship.shipLength() - 1;
            char lastLetter = lettersOrderedList[endLetterNum - 1];
            while(alphabeticPosition <= lastLetter){
                if(board.containsKey(String.valueOf(numericPosition) +  alphabeticPosition) && board.get(String.valueOf(numericPosition) +  alphabeticPosition).withShip()){
                    return true;
                }
                positionsToOccupy[step] = String.valueOf(numericPosition) +  alphabeticPosition;
                alphabeticPosition++;
                step++;
            }
        }
        return false;
    }

    private static char getAlphabeticPosition(String position) {
        return position.substring(position.length() - 1).toLowerCase().charAt(0);
    }

    private static int getNumericPosition(String position) {
        return Integer.parseInt(position.substring(0, position.length() - 1));
    }

}