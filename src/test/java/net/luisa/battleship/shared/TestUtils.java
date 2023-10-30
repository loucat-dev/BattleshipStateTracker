package net.luisa.battleship.shared;

import net.luisa.battleship.domain.TargetSquare;
import net.luisa.battleship.utils.BoardUtils;

import java.util.List;
import java.util.Map;

public class TestUtils {
    private TestUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, TargetSquare> populateBoardWithPositions(List<String> positions){
        Map<String, TargetSquare> board = BoardUtils.populateEmptyBoard();

        positions.forEach(position -> board.put(position, new TargetSquare(true, false)));

        return board;
    }
}
