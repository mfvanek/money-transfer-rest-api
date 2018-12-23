package com.mfvanek.money.transfer.models.parties;

import com.mfvanek.money.transfer.enums.PartyType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

abstract class LegalPerson extends AbstractParty {

    private final String name;

    LegalPerson(Long id, String taxIdentificationNumber, String name) {
        super(id, PartyType.LEGAL_PERSON, taxIdentificationNumber);
        Objects.requireNonNull(name, "Name cannot be null");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(39, 19)
                .append(name)
                .append(getPartyType())
                .append(getTaxIdentificationNumber())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof LegalPerson)) {
            return false;
        }

        LegalPerson other = (LegalPerson) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(getPartyType(), other.getPartyType())
                .append(getTaxIdentificationNumber(), other.getTaxIdentificationNumber())
                .isEquals();
    }
}
