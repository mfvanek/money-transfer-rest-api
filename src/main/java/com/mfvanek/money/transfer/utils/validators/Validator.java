package com.mfvanek.money.transfer.utils.validators;

import com.mfvanek.money.transfer.interfaces.Account;

import java.math.BigDecimal;
import java.util.Collection;

public class Validator {

    public static void validateAmountNotNegative(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative for " + account);
        }
    }

    public static void validateAmountNotNegative(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public static void validateAmountPositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public static void validateCurrencyIsTheSame(Account debit, Account credit) {
        if (!debit.getCurrency().equals(credit.getCurrency())) {
            throw new UnsupportedOperationException(
                    String.format("Multi-currency operations are not supported. Debit account = %s; credit account = %s", debit, credit));
        }
    }

    public static void validateAccountsAreValid(Account debit, Account credit) {
        if (debit.isNotValid()) {
            throw new IllegalArgumentException("Debit account must be valid");
        }

        if (credit.isNotValid()) {
            throw new IllegalArgumentException("Credit account must be valid");
        }
    }

    public static void validateAccountIsDifferent(Account debit, Account credit) {
        if (debit.equals(credit)) {
            throw new IllegalArgumentException("Accounts must be different");
        }
    }

    public static <T> void validatePagination(final int pageNumber, final int recordsPerPage) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number should be positive and starts with 1");
        }

        if (recordsPerPage < 1) {
            throw new IllegalArgumentException("Amount from records per page should be greater than zero");
        }
    }

    public static <T> void validatePageableContentSize(final Collection<T> pageableContent, final int recordsPerPage) {
        if (pageableContent.size() > recordsPerPage + 1) {
            throw new IllegalArgumentException("Pageable content size is too big");
        }
    }
}
