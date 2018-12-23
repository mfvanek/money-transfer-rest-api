package com.mfvanek.money.transfer.interfaces;

public interface Validatable {

    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}
