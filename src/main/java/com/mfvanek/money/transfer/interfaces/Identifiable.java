/*
 * Copyright (c) 2018-2021. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.interfaces;

public interface Identifiable extends Validatable {

    long INVALID_ID = -1L;

    Long getId();

    @Override
    default boolean isValid() {
        return INVALID_ID != getId();
    }
}
