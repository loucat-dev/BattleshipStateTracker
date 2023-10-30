package net.luisa.battleship.service;

import net.luisa.battleship.domain.*;
import net.luisa.battleship.exception.ShipValidationException;
import net.luisa.battleship.utils.BoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleshipServiceTest {

    private static final String VALID_POSITION_1A = "1A";
    private static final Ship VALID_SHIP = new Ship("Carrier", 5);
    private static final Ship VALID_SHIP_SMALL = new Ship("Destroyer", 2);

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
    void testAddShipToBoard_NullParameters_ThrowsValidationException(Ship ship, String position, Direction direction) {
        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(ship, position, direction);
        });
    }

    @Test
    void testAddShipToBoard_InvalidShip_ThrowsValidationException() {
        Ship invalidShip = new Ship("Carrier", 4);

        when(boardGameMock.canShipBeAdded(invalidShip)).thenThrow(ShipValidationException.class);

        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(invalidShip, VALID_POSITION_1A, Direction.HORIZONTAL);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"0A", "11A", "1K", "3l", "l5", "invalid"})
    void testAddShipToBoard_InvalidPosition_ThrowsValidationException(String position) {
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);

        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(VALID_SHIP, position, Direction.HORIZONTAL);
        });
    }

    private static Stream<Arguments> invalidPlacements() {
        return Stream.of(
                Arguments.of(VALID_SHIP_SMALL, "1j", Direction.HORIZONTAL),
                Arguments.of(VALID_SHIP, "1g", Direction.HORIZONTAL),
                Arguments.of(VALID_SHIP_SMALL, "10a", Direction.VERTICAL),
                Arguments.of(VALID_SHIP, "7a", Direction.VERTICAL)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPlacements")
    void testAddShipToBoard_InvalidPlacement_ThrowsValidationException(Ship ship, String position, Direction direction) {
        when(boardGameMock.canShipBeAdded(ship)).thenReturn(true);

        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(ship, position, direction);
        });
    }

    @Test
    void testAddShipToBoard_PositionOccupiedVertical_ThrowsValidationException() {
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);
        Map<String, TargetSquare> board = Map.of("2a", new TargetSquare(true, false), "2b", new TargetSquare(true, false), "2c", new TargetSquare(true, false));
        when(boardGameMock.getBoard()).thenReturn(board);

        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(VALID_SHIP, "1a", Direction.VERTICAL);
        });
    }

    @Test
    void testAddShipToBoard_PositionOccupiedHorizontal_ThrowsValidationException() {
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);
        Map<String, TargetSquare> board = Map.of("8j", new TargetSquare(true, false), "9j", new TargetSquare(true, false), "10j", new TargetSquare(true, false));
        when(boardGameMock.getBoard()).thenReturn(board);

        assertThrows(ShipValidationException.class, () -> {
            battleshipService.addShipOnBoard(VALID_SHIP, "10f", Direction.HORIZONTAL);
        });
    }

    @Test
    void testAddShipToBoard_ValidPlacement_OccupyPositions(){
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);
        when(boardGameMock.getBoard()).thenReturn(BoardUtils.populateEmptyBoard());

        Map<String, TargetSquare> board = battleshipService.addShipOnBoard(VALID_SHIP, "10f", Direction.HORIZONTAL);

        assertThat(board).contains(
                entry("10f", new TargetSquare(true, false)),
                entry("10g", new TargetSquare(true, false)),
                entry("10h", new TargetSquare(true, false)),
                entry("10i", new TargetSquare(true, false)),
                entry("10j", new TargetSquare(true, false)));

        verify(boardGameMock).useShip(VALID_SHIP);
    }

    @Test
    void testAddBattleshipOnBoard_ValidPlacementsList_ReturnsCorrectBoard(){
        when(boardGameMock.canShipBeAdded(VALID_SHIP)).thenReturn(true);
        Map<String, TargetSquare> tempBoard = populateBoardWithPositions(List.of("10f","10g","10h","10i","10j"));
        when(boardGameMock.getBoard()).thenReturn(BoardUtils.populateEmptyBoard(), tempBoard);

        List<ShipPlacement> shipPlacements = List.of(new ShipPlacement(VALID_SHIP, "10f", Direction.HORIZONTAL),
                new ShipPlacement(VALID_SHIP, "1a", Direction.VERTICAL));

        Map<String, TargetSquare> board = battleshipService.addBattleshipOnBoard(shipPlacements);

        assertThat(board).contains(
                entry("10f", new TargetSquare(true, false)),
                entry("10g", new TargetSquare(true, false)),
                entry("10h", new TargetSquare(true, false)),
                entry("10i", new TargetSquare(true, false)),
                entry("10j", new TargetSquare(true, false)));

        verify(boardGameMock, times(2)).canShipBeAdded(VALID_SHIP);
        verify(boardGameMock, times(2)).getBoard();
        verify(boardGameMock, times(2)).useShip(VALID_SHIP);
    }

    private Map<String, TargetSquare> populateBoardWithPositions(List<String> positions){
        Map<String, TargetSquare> board = BoardUtils.populateEmptyBoard();

        positions.forEach(position -> board.put(position, new TargetSquare(true, false)));

        return board;
    }

}
