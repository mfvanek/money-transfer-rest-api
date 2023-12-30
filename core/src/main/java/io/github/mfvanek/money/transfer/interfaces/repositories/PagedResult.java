/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces.repositories;

import java.util.Collection;

public interface PagedResult<T> {

    boolean hasMore();

    Collection<T> getContent();

    int getPageNumber();

    int getRecordsPerPage();
}
