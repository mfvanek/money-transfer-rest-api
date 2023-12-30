/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.exceptions;

import java.math.BigDecimal;

public class InvalidBalanceException extends RuntimeException {

    public InvalidBalanceException(BigDecimal expected, BigDecimal actual) {
        super(String.format("Invalid balance: expected %s, but actual was %s", expected, actual));
    }
}
