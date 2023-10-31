package net.luisa.battleship.domain;

import net.luisa.battleship.exception.ShipValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.luisa.battleship.TestUtils.populateBoardWithPositions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardGameTest {

    private static final Ship VALID_SHIP = Ship.CARRIER;

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
    void testReceiveAttack_SuccessfulAttack_ReturnsCorrectAttackResult(){
        Map<String, TargetSquare> board = populateBoardWithPositions(List.of("10h","10i"));
        boardGame = new BoardGame(board, null, 17, false);

        AttackResult result = boardGame.receiveAttack( "10i");

        assertThat(result).isEqualTo(new AttackResult(true, 16));
    }

    @Test
    void testReceiveAttack_MissedAttack_ReturnsCorrectAttackResult(){
        Map<String, TargetSquare> board = populateBoardWithPositions(List.of("10h","10i"));
        boardGame = new BoardGame(board, null, 17, false);

        AttackResult result = boardGame.receiveAttack("5h");
        assertThat(result).isEqualTo(new AttackResult(false, 17));
    }

    @Test
    void testReceiveAttack_DoubleAttack_ReturnsSameScore(){
        Map<String, TargetSquare> board = populateBoardWithPositions(List.of("10h","10i"));
        boardGame = new BoardGame(board, null, 17, false);

        AttackResult result = boardGame.receiveAttack("10h");
        assertThat(result).isEqualTo(new AttackResult(true, 16));

        AttackResult result2 = boardGame.receiveAttack("10h");
        assertThat(result2).isEqualTo(new AttackResult(false, 16));
    }
}