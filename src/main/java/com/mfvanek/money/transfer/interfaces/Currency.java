/*
 * Copyright (c) 2018-2021. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.interfaces;

public interface Currency extends Validatable {

    int ISO_CODE_LENGTH = 3;

    String getISOCode();
}
