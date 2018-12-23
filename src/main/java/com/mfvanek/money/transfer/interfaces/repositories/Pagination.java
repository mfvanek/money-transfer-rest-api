package com.mfvanek.money.transfer.interfaces.repositories;

import com.mfvanek.money.transfer.interfaces.Validatable;

public interface Pagination extends Validatable {

    int getRecordsPerPage();

    int getPageNumber();
}
