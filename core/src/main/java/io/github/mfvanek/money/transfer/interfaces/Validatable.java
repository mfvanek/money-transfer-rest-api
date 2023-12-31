/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces;

public interface Validatable {

    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}
