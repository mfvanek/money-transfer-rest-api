package com.mfvanek.money.transfer.models.transactions;

import com.mfvanek.money.transfer.interfaces.Identifiable;
import com.mfvanek.money.transfer.models.accounts.AbstractAccount;

import java.math.BigDecimal;

final class InvalidTransaction extends MoneyTransaction {

    private InvalidTransaction() {
        super(Identifiable.INVALID_ID, AbstractAccount.getInvalid(), AbstractAccount.getInvalid(), BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return (obj instanceof InvalidTransaction);
    }

    @Override
    public final boolean run() {
        return false;
    }

    private static class LazyHolder {
        private static final InvalidTransaction INSTANCE = new InvalidTransaction();
    }

    static InvalidTransaction getInstance() {
        return LazyHolder.INSTANCE;
    }
}
