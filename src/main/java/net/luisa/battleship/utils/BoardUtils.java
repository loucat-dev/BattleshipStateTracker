package net.luisa.battleship.utils;

import net.luisa.battleship.domain.TargetSquare;

import java.util.HashMap;
import java.util.Map;

import static net.luisa.battleship.domain.BoardGame.BOARD_SIZE;

public class BoardUtils {

    private BoardUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final Map<Character, Integer> lettersToNumbersMap = Map.of('a', 1, 'b', 2, 'c', 3, 'd', 4, 'e', 5,
            'f', 6, 'g', 7, 'h', 8, 'i', 9, 'j', 10);

    public static final char[] lettersOrderedList = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};

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

    public static char getAlphabeticPosition(String position) { //TODO: to test
        return position.substring(position.length() - 1).toLowerCase().charAt(0);
    }

    public static int getNumericPosition(String position) {
        return Integer.parseInt(position.substring(0, position.length() - 1)); //TODO: to test
    }

}
