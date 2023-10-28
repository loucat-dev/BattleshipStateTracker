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

import java.util.Collections;
import java.util.stream.Stream;


class BattleshipServiceTest {

    private static final String VALID_POSITION_1A = "1A";
    private static final Ship VALID_SHIP = new Ship("Carrier", 5);

    private BattleshipService battleshipService;

    @BeforeEach
    void setUp() {
        battleshipService = new BattleshipService(new BoardGame(Collections.emptyMap()));
    }

    private static Stream<Arguments> nullParameters() {
        return Stream.of(
                Arguments.of(null, VALID_POSITION_1A, Direction.HORIZONTAL),
                Arguments.of(VALID_SHIP, null, Direction.VERTICAL),
                Arguments.of(VALID_SHIP, "", Direction.HORIZONTAL),
                Arguments.of(VALID_SHIP, VALID_POSITION_1A, null)
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
    void testAddShipToBoard_NotExistingShip_ThrowsValidationException(){
        Ship invalidShip = new Ship("Carrier", 4);
        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(invalidShip, VALID_POSITION_1A, Direction.HORIZONTAL);
        });
    }

    @Test
    void testAddShipToBoard_ShipAlreadyAdded_ThrowsValidationException(){
        battleshipService.addShipOnBoard(VALID_SHIP, "1A", Direction.HORIZONTAL);
        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(VALID_SHIP, VALID_POSITION_1A, Direction.HORIZONTAL);
        });
    }
}