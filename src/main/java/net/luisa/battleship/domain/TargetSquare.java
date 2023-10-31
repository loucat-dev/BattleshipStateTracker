package net.luisa.battleship.domain;

import java.util.Objects;

public class TargetSquare {

    private final boolean withShip;
    private final boolean hit;

    public TargetSquare(boolean withShip, boolean hit) {
        this.withShip = withShip;
        this.hit = hit;
    }

    public boolean withShip() {
        return withShip;
    }

    public boolean isHit() {
        return hit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetSquare that = (TargetSquare) o;
        return withShip == that.withShip && hit == that.hit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(withShip, hit);
    }
}
