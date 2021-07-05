/*
 * Copyright (c) 2018-2021. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.interfaces;

public interface Validatable {

    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}
