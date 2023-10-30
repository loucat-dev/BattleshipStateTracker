package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.luisa.battleship.shared.TestUtils.populateBoardWithPositions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardGameTest {

    private static final Ship VALID_SHIP = new Ship("Carrier", 5);
    private static final Ship INVALID_SHIP = new Ship("InvalidShip", 5);

    private BoardGame boardGame;

    @BeforeEach
    void setUp() {
        boardGame = new BoardGame();
    }

    @Test
    void testCanShipBeAdded_ValidShip_ReturnsTrue() {
        assertTrue(boardGame.canShipBeAdded(VALID_SHIP));
    }

    @Test
    void testCanShipBeAdded_InvalidShip_ThrowsException() {
        assertThrows(ShipValidationException.class, () -> {
            boardGame.canShipBeAdded(INVALID_SHIP);
        });
    }

    @Test
    void testCanShipBeAdded_ShipAlreadyPlaced_ThrowsException() {
        Map<Ship, Boolean> shipsMap = new HashMap<>();
        shipsMap.put(VALID_SHIP, true);

        BoardGame boardGameWithShip = new BoardGame(shipsMap);

        assertThrows(ShipValidationException.class, () -> {
            boardGameWithShip.canShipBeAdded(VALID_SHIP);
        });
    }

    @Test
    void testAddShipOnBoard_ValidShip_ShipMapIsUpdated() {
        boardGame.useShip(VALID_SHIP);

        assertThat(boardGame.getShipsOnBoard().get(VALID_SHIP)).isTrue();
    }

    @Test
    void testAddShipOnBoard_InvalidShip_ThrowsException() {
        assertThrows(ShipValidationException.class, () -> {
            boardGame.useShip(INVALID_SHIP);
        });
    }

    @Test
    void testReceiveAttack_HorizontalSuccessfulAttack_ReturnsCorrectAttackResult(){ //TODO: also test miss, also test vertical
        Map<String, TargetSquare> board = populateBoardWithPositions(List.of("10h","10i"));
        boardGame = new BoardGame(board, null, 17, false);

        AttackResult result = boardGame.receiveAttack(new ShipPlacement(VALID_SHIP, "10f", Direction.HORIZONTAL));
        assertThat(result.hit()).isTrue();
        assertThat(result.hitPositions()).contains("10h","10i"); //TODO: change to List so that we don't have null values
    }
}