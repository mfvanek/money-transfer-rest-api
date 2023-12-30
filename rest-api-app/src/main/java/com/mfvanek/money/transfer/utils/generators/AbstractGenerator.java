/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.utils.generators;

import com.mfvanek.money.transfer.repositories.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
abstract class AbstractGenerator {

    private static final int AWAIT_PERIOD = 10; // in seconds

    final AtomicInteger counter;
    final Context context;
    final Collection<Long> ids;
    private final String message;
    private final int threadPoolSize;

    AbstractGenerator(final Context context, final String message, final int threadPoolSize) {
        Objects.requireNonNull(context, "Context cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        this.counter = new AtomicInteger(0);
        this.context = context;
        this.ids = new ConcurrentLinkedQueue<>();
        this.message = message;
        this.threadPoolSize = threadPoolSize;
    }

    AbstractGenerator(final Context context, final String message) {
        this(context, message, Runtime.getRuntime().availableProcessors());
    }

    abstract List<Future<?>> doGenerate(final ExecutorService threadPool);

    final List<Long> generate() {
        final long timeStart = System.nanoTime();
        try {
            log.info("Generating {}", message);
            final ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);
            final List<Future<?>> futures = doGenerate(threadPool);
            threadPool.shutdown();
            waitForCompletion(futures);
        } finally {
            final long timeEnd = System.nanoTime();
            log.info("Generation {} is completed. Time elapsed = {} microseconds", message, (timeEnd - timeStart) / 1_000);
        }
        return List.copyOf(ids);
    }

    private void waitForCompletion(final List<Future<?>> futures) {
        log.info("Waiting for completion from {} tasks...", futures.size());
        int processed = 0;
        int batch = 0;
        final int threshold = futures.size() / 10;
        for (final Future<?> future : futures) {
            try {
                future.get(AWAIT_PERIOD, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error("Error occurred while awaiting", e);
            } finally {
                ++processed;
                ++batch;
            }
            if (batch >= threshold) {
                final int percentage = processed * 100 / futures.size();
                log.info("Completed {}%...", percentage);
                batch = 0;
            }
        }
    }
}
