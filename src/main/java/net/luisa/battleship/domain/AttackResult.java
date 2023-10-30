package net.luisa.battleship.domain;

import java.util.List;

public record AttackResult(boolean hit, String[] hitPositions) {
}
