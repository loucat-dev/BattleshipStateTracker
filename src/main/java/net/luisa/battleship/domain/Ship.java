package net.luisa.battleship.domain;

public enum Ship {

    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2);

    public final int shipLength;

    Ship(int shipLength) {
        this.shipLength = shipLength;
    }

    public int getShipLength() {
        return shipLength;
    }
}
