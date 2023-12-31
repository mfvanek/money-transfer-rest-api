/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces.repositories;

import io.github.mfvanek.money.transfer.interfaces.Account;
import io.github.mfvanek.money.transfer.interfaces.Transaction;

import java.math.BigDecimal;

public interface TransactionRepository extends Repository<Transaction>, Cleanable {

    Transaction add(Account debit, Account credit, BigDecimal amount);

    PagedResult<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage);

    default PagedResult<Transaction> getByAccount(Account account, Pagination pagination) {
        return getByAccount(account, pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
