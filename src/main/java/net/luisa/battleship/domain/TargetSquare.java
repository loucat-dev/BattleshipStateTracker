package net.luisa.battleship.domain;

public class TargetSquare {

    private boolean withShip;
    private boolean hit;

    public TargetSquare(boolean withShip, boolean hit) {
        this.withShip = withShip;
        this.hit = hit;
    }

    public boolean withShip() {
        return withShip;
    }

    public void setWithShip(boolean withShip) {
        this.withShip = withShip;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
