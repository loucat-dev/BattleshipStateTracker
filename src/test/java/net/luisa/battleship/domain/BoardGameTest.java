package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BoardGameTest {

    private static final Ship VALID_SHIP = new Ship("Carrier", 5);
    private static final Ship INVALID_SHIP = new Ship("InvalidShip", 5);

    private BoardGame boardGame;

    @BeforeEach
    void setUp() {
        boardGame = new BoardGame();
    }

    @Test
    void testCanShipBeAdded_ValidShip_ReturnsTrue(){
        assertTrue(boardGame.canShipBeAdded(VALID_SHIP));
    }

    @Test
    void testCanShipBeAdded_InvalidShip_ThrowsException(){
        assertThrows(ShipValidationException.class, () -> {
            boardGame.canShipBeAdded(INVALID_SHIP);
        });
    }

    @Test
    void testCanShipBeAdded_ShipAlreadyPlaced_ThrowsException(){
        Map<Ship, Boolean> shipsMap = new HashMap<>();
        shipsMap.put(VALID_SHIP, true);

        BoardGame boardGameWithShip = new BoardGame(shipsMap);

        assertThrows(ShipValidationException.class, () -> {
            boardGameWithShip.canShipBeAdded(VALID_SHIP);
        });
    }

    @Test
    void testAddShipOnBoard_ValidShip_ShipMapIsUpdated(){
        boardGame.addShipOnBoard(VALID_SHIP);

        assertThat(boardGame.getShipsOnBoard().get(VALID_SHIP)).isTrue();
    }

    @Test
    void testAddShipOnBoard_InvalidShip_ThrowsException(){
        assertThrows(ShipValidationException.class, () -> {
            boardGame.addShipOnBoard(INVALID_SHIP);
        });
    }
}