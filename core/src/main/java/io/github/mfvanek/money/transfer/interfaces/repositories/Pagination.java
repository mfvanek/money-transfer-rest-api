/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces.repositories;

import io.github.mfvanek.money.transfer.interfaces.Validatable;

public interface Pagination extends Validatable {

    int getRecordsPerPage();

    int getPageNumber();
}
