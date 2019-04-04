package com.hieugie.banthe.domain.enumeration;

/**
 * The Price enumeration.
 */
public enum Price {
    DENO10(10000), DENO20(20000), DENO50(50000), DENO100(100000), DENO200(200000), DENO500(500000), DENO1000(1000000);

    private final int value;

    Price(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
