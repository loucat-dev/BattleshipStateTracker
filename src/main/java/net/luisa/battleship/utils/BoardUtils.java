package net.luisa.battleship.utils;

import net.luisa.battleship.domain.TargetSquare;

import java.util.HashMap;
import java.util.Map;

import static net.luisa.battleship.domain.BoardGame.BOARD_SIZE;

public class BoardUtils {

    private BoardUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, TargetSquare> populateEmptyBoard(){
        Map<String, TargetSquare> board = new HashMap<>();

        for (int position1 = 1; position1 <= BOARD_SIZE ; position1++) {
            for (char position2 = 'a'; position2 <= 'j'; position2++) {
                String numericalPosition = String.valueOf(position1);
                String alphabeticalPosition = Character.toString(position2);
                board.put(numericalPosition +  alphabeticalPosition, new TargetSquare(false, false));
            }
        }
        return board;
    }
}
