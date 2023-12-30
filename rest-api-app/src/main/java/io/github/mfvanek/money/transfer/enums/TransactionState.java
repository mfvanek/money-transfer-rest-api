/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.enums;

public enum TransactionState {

    NEW,
    INSUFFICIENT_FUNDS,
    COMPLETED,
    CONCURRENCY_ERROR,
    RESTARTED
}
