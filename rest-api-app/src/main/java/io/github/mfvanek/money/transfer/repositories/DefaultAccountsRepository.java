/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.repositories;

import io.github.mfvanek.money.transfer.exceptions.InvalidBalanceException;
import io.github.mfvanek.money.transfer.interfaces.Account;
import io.github.mfvanek.money.transfer.interfaces.Currency;
import io.github.mfvanek.money.transfer.interfaces.Party;
import io.github.mfvanek.money.transfer.interfaces.repositories.AccountsRepository;
import io.github.mfvanek.money.transfer.interfaces.repositories.PagedResult;
import io.github.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import io.github.mfvanek.money.transfer.models.accounts.AbstractAccount;
import io.github.mfvanek.money.transfer.models.currencies.BaseCurrency;
import io.github.mfvanek.money.transfer.utils.validators.Validator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
final class DefaultAccountsRepository implements AccountsRepository {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100_000_000.00d);

    private final AtomicLong counter;
    private final ConcurrentMap<Long, Account> accounts;
    private final PartyRepository partyRepository;
    private final Long ourBankAccountId;

    DefaultAccountsRepository(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
        this.counter = new AtomicLong(0L);
        this.accounts = new ConcurrentHashMap<>();
        final Account ourBankAccount = addOurBankAccount("20202810100000010001", INITIAL_BALANCE);
        this.ourBankAccountId = ourBankAccount.getId();
    }

    @Override
    public Account getById(Long id) {
        return accounts.getOrDefault(id, getInvalid());
    }

    @Override
    public Account getInvalid() {
        return AbstractAccount.getInvalid();
    }

    @Override
    public Account addOurBankAccount(String number, BigDecimal balance) {
        return addOurBankAccount(BaseCurrency.getDefault(), number, balance);
    }

    @Override
    public Account addOurBankAccount(Currency currency, String number, BigDecimal balance) {
        // TODO Add unique index for account number + currency
        final Account account = AbstractAccount.makeActiveAccount(
                counter.incrementAndGet(), currency, number, partyRepository.getOurBank(), balance);
        accounts.putIfAbsent(account.getId(), account);
        return account;
    }

    @Override
    public BigDecimal getInitialBalance() {
        return INITIAL_BALANCE;
    }

    @Override
    public Account getOurBankMainAccount() {
        // by design
        return getById(ourBankAccountId);
    }

    @Override
    public Account addPassiveAccount(Currency currency, String number, Party holder) {
        final Account account = AbstractAccount.makePassiveAccount(counter.incrementAndGet(), currency, number, holder);
        accounts.putIfAbsent(account.getId(), account);
        return account;
    }

    @Override
    public Account addPassiveAccount(String number, Party holder) {
        return addPassiveAccount(BaseCurrency.getDefault(), number, holder);
    }

    @Override
    public int size() {
        return accounts.size();
    }

    @Override
    public void validateBalance() {
        final long timeStart = System.nanoTime();
        try {
            final BigDecimal expected = getInitialBalance();
            BigDecimal totalSum = BigDecimal.ZERO;
            for (Account a : accounts.values()) {
                Validator.validateAmountNotNegative(a);
                totalSum = totalSum.add(a.getBalance());
            }
            if (totalSum.compareTo(expected) != 0) {
                throw new InvalidBalanceException(expected, totalSum);
            }
            log.debug("Balance is valid! {} == {}", expected, totalSum);
        } finally {
            final long timeEnd = System.nanoTime();
            log.info("Balance validation is completed. Time elapsed = {} microseconds", (timeEnd - timeStart) / 1_000);
        }
    }

    @Override
    public PagedResult<Account> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultImpl.from(pageNumber, recordsPerPage, accounts);
    }

    @Override
    public Collection<Account> getByHolder(Party holder) {
        return accounts.values().stream()
                .filter(a -> a.getHolder().equals(holder))
                .sorted(Comparator.comparing(Account::getId))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void clear() {
        // TODO Initial balance is not restored
        accounts.keySet().removeIf(k -> !k.equals(ourBankAccountId));
        counter.set(ourBankAccountId);
    }
}
