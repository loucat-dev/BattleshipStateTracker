package net.luisa.battleship.service;

import net.luisa.battleship.domain.BoardGame;
import net.luisa.battleship.domain.Direction;
import net.luisa.battleship.domain.Ship;
import net.luisa.battleship.exception.ShipValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleshipServiceTest {

    private static final String VALID_POSITION_1A = "1A";
    private static final Ship VALID_SHIP = new Ship("Carrier", 5);

    @Mock
    private BoardGame boardGameMock;

    private BattleshipService battleshipService;

    @BeforeEach
    void setUp() {
        battleshipService = new BattleshipService(boardGameMock);
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
    void testAddShipToBoard_InvalidShip_ThrowsValidationException(){
        Ship invalidShip = new Ship("Carrier", 4);

        when(boardGameMock.canShipBeAdded(invalidShip)).thenThrow(ShipValidationException.class);

        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(invalidShip, VALID_POSITION_1A, Direction.HORIZONTAL);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"0A", "11A", "1K", "3l", "l5", "invalid"})
    void testAddShipToBoard_InvalidPosition_ThrowsValidationException(String position){
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);

        Assertions.assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(VALID_SHIP, position, Direction.HORIZONTAL);
        });
    }
}