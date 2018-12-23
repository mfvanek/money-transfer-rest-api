package com.mfvanek.money.transfer.interfaces.repositories;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Transaction;

import java.math.BigDecimal;

public interface TransactionRepository extends Repository<Transaction> {

    Transaction add(Account debit, Account credit, BigDecimal amount);

    PagedResult<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage);

    default PagedResult<Transaction> getByAccount(Account account, Pagination pagination) {
        return getByAccount(account, pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
