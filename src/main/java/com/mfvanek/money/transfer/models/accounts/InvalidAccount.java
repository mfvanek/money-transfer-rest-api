package com.mfvanek.money.transfer.models.accounts;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Identifiable;
import com.mfvanek.money.transfer.models.currencies.BaseCurrency;
import com.mfvanek.money.transfer.models.parties.AbstractParty;

final class InvalidAccount extends AbstractAccount {

    private InvalidAccount() {
        super(Identifiable.INVALID_ID, BaseCurrency.getInvalid(), "", AbstractParty.getInvalid(), false);
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

        return (obj instanceof InvalidAccount);
    }

    @Override
    public String toString() {
        final String base = super.toString();
        return base.replace("Account{", "InvalidAccount{");
    }

    private static class LazyHolder {
        private static final InvalidAccount INSTANCE = new InvalidAccount();
    }

    static Account getInstance() {
        return LazyHolder.INSTANCE;
    }
}
