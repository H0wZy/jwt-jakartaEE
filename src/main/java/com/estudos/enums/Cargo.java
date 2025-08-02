package com.estudos.enums;

public enum Cargo {
    OPERATIONAL(1),
    MOD(2),
    ADMIN(3);


    private final int value;

    Cargo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Cargo fromValue(int value) {
        for (Cargo cargo : Cargo.values()) {
            if (cargo.getValue() == value) {
                return cargo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para Cargo: " + value);
    }
}
