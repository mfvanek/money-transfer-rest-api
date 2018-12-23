package com.mfvanek.money.transfer.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class InvalidBalanceException extends RuntimeException {

    private final BigDecimal expected;
    private final BigDecimal actual;

    public InvalidBalanceException(BigDecimal expected, BigDecimal actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
