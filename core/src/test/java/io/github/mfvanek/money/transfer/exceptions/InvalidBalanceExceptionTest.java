package io.github.mfvanek.money.transfer.exceptions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidBalanceExceptionTest {

    @Test
    void errorMessageIsCorrect() {
        assertThat(new InvalidBalanceException(BigDecimal.ZERO, BigDecimal.TEN))
                .hasMessage("Invalid balance: expected 0, but actual was 10");
    }
}
