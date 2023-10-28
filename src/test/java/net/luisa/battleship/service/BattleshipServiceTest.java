package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.exception.ShipValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class BattleshipServiceTest {

    private static final String VALID_POSITION_1A = "1A";

    private BattleshipService battleshipService;

    @BeforeEach
    void setUp() {
        battleshipService = new BattleshipService(new BoardGame());
    }

    private static Stream<Arguments> nullParameters() {
        return Stream.of(
                Arguments.of(null, VALID_POSITION_1A, Direction.HORIZONTAL),
                Arguments.of(new Ship(2), null, Direction.VERTICAL),
                Arguments.of(new Ship(3), "", Direction.HORIZONTAL),
                Arguments.of(new Ship(2), VALID_POSITION_1A, null)
        );
    }

    @ParameterizedTest
    @MethodSource("nullParameters")
    void testAddShipToBoard_NullParameters_ThrowsValidationException(Ship ship, String position, Direction direction){
        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(ship, position, direction);
        });
    }

    @Test
    void testAddShipToBoard_NullPosition_ThrowsValidationException(){
        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(null, VALID_POSITION_1A, Direction.HORIZONTAL);
        });
    }
}