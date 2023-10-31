package net.luisa.battleship.integration;

import net.luisa.battleship.domain.*;
import net.luisa.battleship.service.BattleshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BattleshipIntegrationTest {


    private BattleshipService battleshipService;
    private BoardGame boardGame;

    @BeforeEach
    void setUp() {
        boardGame = new BoardGame();
        battleshipService = new BattleshipService(boardGame);
    }

    @Test
    void testEnd2endBattle(){

        List<ShipPlacement> shipPlacements = List.of(
                new ShipPlacement(new Ship("Carrier", 5), "2b", Direction.HORIZONTAL),
                new ShipPlacement(new Ship("Battleship", 4), "2a", Direction.VERTICAL),
                new ShipPlacement(new Ship("Cruiser", 3), "10f", Direction.HORIZONTAL),
                new ShipPlacement(new Ship("Submarine", 3), "5b", Direction.VERTICAL),
                new ShipPlacement(new Ship("Destroyer", 2), "4h", Direction.HORIZONTAL));
//TODO: I think the ships should be enum


        // position the ships to the board
        battleshipService.addBattleshipOnBoard(shipPlacements);

        // send the first 5 successful attacks
        assertThat(boardGame.receiveAttack("2b")).isEqualTo(new AttackResult(true, 16));
        assertThat(boardGame.receiveAttack("2c")).isEqualTo(new AttackResult(true, 15));
        assertThat(boardGame.receiveAttack("2d")).isEqualTo(new AttackResult(true, 14));
        assertThat(boardGame.receiveAttack("2e")).isEqualTo(new AttackResult(true, 13));
        assertThat(boardGame.receiveAttack("2f")).isEqualTo(new AttackResult(true, 12));

        // send 1 unsuccessful and 4 successful attacks
        assertThat(boardGame.receiveAttack("1a")).isEqualTo(new AttackResult(false, 12));
        assertThat(boardGame.receiveAttack("2a")).isEqualTo(new AttackResult(true, 11));
        assertThat(boardGame.receiveAttack("3a")).isEqualTo(new AttackResult(true, 10));
        assertThat(boardGame.receiveAttack("4a")).isEqualTo(new AttackResult(true, 9));
        assertThat(boardGame.receiveAttack("5a")).isEqualTo(new AttackResult(true, 8));

        // send 7 successful attacks
        assertThat(boardGame.receiveAttack("5b")).isEqualTo(new AttackResult(true, 7));
        assertThat(boardGame.receiveAttack("6b")).isEqualTo(new AttackResult(true, 6));
        assertThat(boardGame.receiveAttack("7b")).isEqualTo(new AttackResult(true, 5));
        assertThat(boardGame.receiveAttack("4h")).isEqualTo(new AttackResult(true, 4));
        assertThat(boardGame.receiveAttack("4i")).isEqualTo(new AttackResult(true, 3));
        assertThat(boardGame.receiveAttack("10f")).isEqualTo(new AttackResult(true, 2));
        assertThat(boardGame.receiveAttack("10g")).isEqualTo(new AttackResult(true, 1));

        assertThat(boardGame.hasLost()).isFalse();

        // send last attack and check hasLost is true
        assertThat(boardGame.receiveAttack("10h")).isEqualTo(new AttackResult(true, 0));
        assertThat(boardGame.hasLost()).isTrue();


    }//TODO: what happens to the score if I hit the boat twice?

    //TODO: shall I stop the game if the game is over?
}
