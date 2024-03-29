/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.models.transactions;

import io.github.mfvanek.money.transfer.consts.Consts;
import io.github.mfvanek.money.transfer.enums.TransactionState;
import io.github.mfvanek.money.transfer.interfaces.Account;
import io.github.mfvanek.money.transfer.interfaces.Transaction;
import io.github.mfvanek.money.transfer.utils.validators.Validator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

// TODO Add DateCreated and LastModifiedDate
@Slf4j
public class MoneyTransaction implements Transaction {

    private final Long id;
    private final Account debit;
    private final Account credit;
    private final BigDecimal amount;
    private TransactionState state;

    MoneyTransaction(Long id, Account debit, Account credit, BigDecimal amount) {
        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(debit, "Debit account cannot be null");
        Objects.requireNonNull(credit, "Credit account cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountPositive(amount);
        Validator.validateAccountsAreValid(debit, credit);
        Validator.validateAccountIsDifferent(debit, credit);
        // TODO Support multi-currency operations
        Validator.validateCurrencyIsTheSame(debit, credit);

        this.id = id;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.state = TransactionState.NEW;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Account getDebit() {
        return debit;
    }

    @Override
    public Account getCredit() {
        return credit;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public synchronized TransactionState getState() {
        return state;
    }

    @Override
    public synchronized boolean run() {
        if (state != TransactionState.COMPLETED) {
            changeState();
            return doRun();
        }
        return false;
    }

    private boolean doRun() {
        final Lock debitLock = debit.writeLock();
        try {
            if (debitLock.tryLock(Consts.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    final Lock creditLock = credit.writeLock();
                    if (creditLock.tryLock(Consts.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                        try {
                            if (debit.debit(amount)) {
                                if (credit.credit(amount)) {
                                    state = TransactionState.COMPLETED;
                                    log.trace("Transaction {} completed", id);
                                    return true;
                                }
                            }
                            state = TransactionState.INSUFFICIENT_FUNDS;
                        } finally {
                            creditLock.unlock();
                        }
                    } else {
                        state = TransactionState.CONCURRENCY_ERROR;
                    }
                } finally {
                    debitLock.unlock();
                }
            } else {
                state = TransactionState.CONCURRENCY_ERROR;
            }
        } catch (InterruptedException e) {
            state = TransactionState.CONCURRENCY_ERROR;
            log.error("Error occurred inside transaction", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    private void changeState() {
        switch (state) {
            case INSUFFICIENT_FUNDS:
            case CONCURRENCY_ERROR:
                state = TransactionState.RESTARTED;
                break;
            default:
                // nothing to do
                break;
        }
    }

    public static Transaction getInvalid() {
        return InvalidTransaction.getInstance();
    }

    public static Transaction make(Long id, Account debit, Account credit, BigDecimal amount) {
        return new MoneyTransaction(id, debit, credit, amount);
    }
}
