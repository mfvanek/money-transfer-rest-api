package com.mfvanek.money.transfer.models.parties;

import com.mfvanek.money.transfer.enums.PartyType;
import com.mfvanek.money.transfer.interfaces.Identifiable;

final class InvalidParty extends AbstractParty {

    private InvalidParty() {
        super(Identifiable.INVALID_ID, PartyType.LEGAL_PERSON, "");
    }

    @Override
    public String getName() {
        return "";
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

        return (obj instanceof InvalidParty);
    }

    private static class LazyHolder {
        private static final InvalidParty INSTANCE = new InvalidParty();
    }

    static InvalidParty getInstance() {
        return LazyHolder.INSTANCE;
    }
}
