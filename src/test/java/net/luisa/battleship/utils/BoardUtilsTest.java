package net.luisa.battleship.utils;

import net.luisa.battleship.domain.TargetSquare;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class BoardUtilsTest {

    @Test
    void testPopulateEmptyBoard_ReturnsTheRightBoard() {
        Map<String, TargetSquare> board = BoardUtils.populateEmptyBoard();

        assertThat(board).contains(
                entry("1a", new TargetSquare(false, false)),
                entry("10a", new TargetSquare(false, false)),
                entry("1j", new TargetSquare(false, false)),
                entry("10j", new TargetSquare(false, false)));

        assertThat(board).doesNotContain(entry("0a", new TargetSquare(false, false)),
                entry("11a", new TargetSquare(false, false)),
                entry("1k", new TargetSquare(false, false)),
                entry("10k", new TargetSquare(false, false)));

        assertThat(board).hasSize(100);
    }
}