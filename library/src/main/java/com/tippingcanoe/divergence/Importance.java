package com.tippingcanoe.divergence;

public enum Importance {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    CRITICAL(-1);

    int rank;

    Importance(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
