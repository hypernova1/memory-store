package org.sam.store.common.lock;

import org.junit.jupiter.api.Test;
import org.sam.store.common.repository.Counter;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryLockManagerMultiThreadTest extends MemoryLockManagerTest {

    private static final int LOOP_COUNT = 1000000;

    @Test
    void race_condition() throws InterruptedException {
        Counter counter = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                counter.increase();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                counter.increase();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                counter.increase();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertThat(counter.getCount()).isNotEqualTo(3 * LOOP_COUNT);
    }

    @Test
    void concurrency_test() throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                memoryLockManager.acquire(LOCK_KEY);
                counter.increase();
                memoryLockManager.release(LOCK_KEY);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                memoryLockManager.acquire(LOCK_KEY);
                counter.increase();
                memoryLockManager.release(LOCK_KEY);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                memoryLockManager.acquire(LOCK_KEY);
                counter.increase();
                memoryLockManager.release(LOCK_KEY);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertThat(counter.getCount()).isEqualTo(3 * LOOP_COUNT);
    }

    @Test
    void call_count() throws InterruptedException {
        class TestMemoryLockManager extends MemoryLockManager {
            public final AtomicInteger setCallCount = new AtomicInteger(0);
            public final AtomicInteger releaseCallCount = new AtomicInteger(0);

            @Override
            public void set(String id) {
                super.set(id);
                this.setCallCount.incrementAndGet();
            }

            @Override
            public void release(String id) {
                super.release(id);
                this.releaseCallCount.incrementAndGet();
            }

            public int getSetCallCount() {
                return this.setCallCount.get();
            }

            public int getReleaseCallCount() {
                return this.releaseCallCount.get();
            }
        }

        TestMemoryLockManager lockManager = new TestMemoryLockManager();

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                lockManager.acquire(LOCK_KEY);
                counter.increase();
                lockManager.release(LOCK_KEY);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                lockManager.acquire(LOCK_KEY);
                counter.increase();
                lockManager.release(LOCK_KEY);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < LOOP_COUNT; i++) {
                lockManager.acquire(LOCK_KEY);
                counter.increase();
                lockManager.release(LOCK_KEY);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertThat(lockManager.getSetCallCount()).isEqualTo(3 * LOOP_COUNT);
        assertThat(lockManager.getReleaseCallCount()).isEqualTo(3 * LOOP_COUNT);
    }
}
