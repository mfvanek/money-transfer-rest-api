package com.mfvanek.money.transfer.models.parties;

final class RussianLegalPerson extends LegalPerson {

    // Without 'Reason code from registration' (КПП)

    RussianLegalPerson(Long id, String taxIdentificationNumber, String name) {
        super(id, taxIdentificationNumber, name);
        validateTaxIdentificationNumber(taxIdentificationNumber);
    }

    private static void validateTaxIdentificationNumber(String taxIdentificationNumber) {
        if (taxIdentificationNumber.length() != 10) {
            throw new IllegalArgumentException("Tax identification number must contain 10 characters");
        }
    }
}
