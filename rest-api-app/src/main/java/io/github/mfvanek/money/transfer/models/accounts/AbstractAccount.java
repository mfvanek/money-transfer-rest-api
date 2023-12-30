/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.models.accounts;

import io.github.mfvanek.money.transfer.consts.Consts;
import io.github.mfvanek.money.transfer.interfaces.Account;
import io.github.mfvanek.money.transfer.interfaces.Currency;
import io.github.mfvanek.money.transfer.interfaces.Party;
import io.github.mfvanek.money.transfer.utils.validators.Validator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class AbstractAccount implements Account {

    private final Long id;
    private final Currency currency;
    private final String number;
    private final Party holder;
    private final boolean active;
    private BigDecimal balance;
    private final transient Lock lock;

    AbstractAccount(Long id, Currency currency, String number,
                    Party holder, boolean active, BigDecimal balance) {
        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        Objects.requireNonNull(number, "Number cannot be null");
        Objects.requireNonNull(holder, "Holder cannot be null");
        Objects.requireNonNull(balance, "Balance cannot be null");
        Validator.validateAmountNotNegative(balance);

        this.id = id;
        this.currency = currency;
        this.number = number;
        this.holder = holder;
        this.active = active;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    @Override
    public final Long getId() {
        return id;
    }

    @Override
    public final String getNumber() {
        return number;
    }

    @Override
    public final Currency getCurrency() {
        return currency;
    }

    @Override
    public final BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean debit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(Consts.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    if (balance.compareTo(amount) > 0) {
                        balance = balance.subtract(amount);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("Error occurred while debiting account", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    @Override
    public boolean credit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(Consts.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    balance = balance.add(amount);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("Error occurred while crediting account", e);
            Thread.currentThread().interrupt();
        }
        return true;
    }

    @Override
    public final Party getHolder() {
        return holder;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Lock writeLock() {
        return lock;
    }

    @Override
    public String toString() {
        return String.format("Account{id=%d, currency=%s, number=%s, active=%s, balance=%s, holder=%s}",
                id, currency, number, active, balance, holder);
    }

    public static Account getInvalid() {
        return InvalidAccount.getInstance();
    }

    public static Account makeActiveAccount(Long id, Currency currency, String number, Party holder, BigDecimal balance) {
        return RussianAccount.makeActiveBalance(id, currency, number, holder, balance);
    }

    public static Account makeActiveAccount(Long id, Currency currency, String number, Party holder) {
        return RussianAccount.makeActiveBalance(id, currency, number, holder);
    }

    public static Account makePassiveAccount(Long id, Currency currency, String number, Party holder) {
        return RussianAccount.makeActiveBalance(id, currency, number, holder);
    }

    public static Account makeActiveAccount(Long id, String number, Party holder) {
        return RussianAccount.makeActiveRouble(id, number, holder);
    }

    public static Account makePassiveAccount(Long id, String number, Party holder) {
        return RussianAccount.makeActiveRouble(id, number, holder);
    }
}
