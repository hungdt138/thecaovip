package com.hieugie.banthe.domain.enumeration;

/**
 * The Action enumeration.
 */
public enum Action {
    TRANSFER(0);

    private final int value;

    Action(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
