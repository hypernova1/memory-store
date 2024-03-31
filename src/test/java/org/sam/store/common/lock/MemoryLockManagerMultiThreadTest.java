package org.sam.store.common.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryLockManagerMultiThreadTest extends MemoryLockManagerTest {

    private static final int NUMBER_OR_THREADS = 500;
    private static final int LOOP_COUNT = 10000;

    @Test
    void race_condition() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OR_THREADS);
        CountDownLatch latch = new CountDownLatch(NUMBER_OR_THREADS);

        Counter counter = new Counter();
        for (int i = 0; i < NUMBER_OR_THREADS; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < LOOP_COUNT; j++) {
                    counter.increase();
                }
                latch.countDown();
            });
        }

        latch.await();
        assertThat(counter.getCount()).isNotEqualTo(NUMBER_OR_THREADS * LOOP_COUNT);
    }

    @Test
    void concurrency_test() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OR_THREADS);
        CountDownLatch latch = new CountDownLatch(NUMBER_OR_THREADS);

        Counter counter = new Counter();
        for (int i = 0; i < NUMBER_OR_THREADS; i++) {
            executorService.execute(() -> {
                memoryLockManager.acquire(LOCK_KEY);
                for (int j = 0; j < LOOP_COUNT; j++) {
                    counter.increase();
                }
                memoryLockManager.release(LOCK_KEY);
                latch.countDown();
            });
        }

        latch.await();
        assertThat(counter.getCount()).isEqualTo(NUMBER_OR_THREADS * LOOP_COUNT);
    }

    @Test
    void call_count() throws InterruptedException {
        class TestMemoryLockManager extends MemoryLockManager {
            public final AtomicInteger setCallCount = new AtomicInteger(0);
            public final AtomicInteger releaseCallCount = new AtomicInteger(0);

            @Override
            public  void set(String id) {
                super.set(id);
                this.setCallCount.incrementAndGet();
            }

            @Override
            public void release(String id) {
                super.release(id);
                this.releaseCallCount.incrementAndGet();
            }
        }

        TestMemoryLockManager testMemoryLockManager = new TestMemoryLockManager();

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OR_THREADS);
        CountDownLatch latch = new CountDownLatch(NUMBER_OR_THREADS);

        Counter counter = new Counter();
        for (int i = 0; i < NUMBER_OR_THREADS; i++) {
            executorService.execute(() -> {
                testMemoryLockManager.acquire(LOCK_KEY);
                for (int j = 0; j < 10000; j++) {
                    counter.increase();
                }
                testMemoryLockManager.release(LOCK_KEY);
                latch.countDown();
            });
        }

        latch.await();

        assertThat(testMemoryLockManager.setCallCount.get()).isEqualTo(NUMBER_OR_THREADS);
        assertThat(testMemoryLockManager.releaseCallCount.get()).isEqualTo(NUMBER_OR_THREADS);
    }
}
