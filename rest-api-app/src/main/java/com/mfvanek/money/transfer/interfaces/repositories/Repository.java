/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.interfaces.repositories;

public interface Repository<T> extends Pageable<T> {

    int size();

    T getById(Long id);

    T getInvalid();
}
