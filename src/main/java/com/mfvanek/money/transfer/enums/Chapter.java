package com.mfvanek.money.transfer.enums;

import java.util.Objects;

public enum Chapter {

    BALANCE("А", "Балансовые счета"),
    TRUST_MANAGEMENT("Б", "Счета доверительного управления"),
    OFF_BALANCE("В", "Внебалансовые счета");

    private final String code;
    private final String description;

    Chapter(String code, String description) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(description);

        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
