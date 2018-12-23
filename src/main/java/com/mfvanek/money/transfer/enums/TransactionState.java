package com.mfvanek.money.transfer.enums;

public enum TransactionState {

    NEW,
    INSUFFICIENT_FUNDS,
    COMPLETED,
    CONCURRENCY_ERROR,
    RESTARTED
}
