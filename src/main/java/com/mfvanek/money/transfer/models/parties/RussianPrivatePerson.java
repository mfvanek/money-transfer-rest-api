package com.mfvanek.money.transfer.models.parties;

class RussianPrivatePerson extends PrivatePerson {

    RussianPrivatePerson(Long id, String taxIdentificationNumber, String firstName, String lastName) {
        super(id, taxIdentificationNumber, firstName, lastName);
        validateTaxIdentificationNumber(taxIdentificationNumber);
    }

    private static void validateTaxIdentificationNumber(String taxIdentificationNumber) {
        if (taxIdentificationNumber.length() != 12) {
            throw new IllegalArgumentException("Tax identification number must contain 12 characters");
        }
    }
}
