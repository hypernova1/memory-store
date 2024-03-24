package org.sam.store.common.lock;

import org.junit.jupiter.api.Test;
import org.sam.store.common.repository.Counter;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryLockManagerMultiThreadTest extends MemoryLockManagerTest {
    @Test
    void race_condition() throws InterruptedException {
        int numberOfIteration = 1000000;

        Counter counter = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                counter.increase();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                counter.increase();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                counter.increase();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println(counter.getCount());
        assertThat(counter.getCount()).isNotEqualTo(3 * numberOfIteration);
    }

    //TODO: 락을 걸지 않고 실행했을 때보다 오차는 확연히 줄었지만, 정확하게 카운트 되지 않음
    @Test
    void concurrency_test() throws InterruptedException {
        int numberOfIteration = 1000000;

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                memoryLockManager.acquire(DEFAULT_KEY);
                counter.increase();
                memoryLockManager.release(DEFAULT_KEY);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                memoryLockManager.acquire(DEFAULT_KEY);
                counter.increase();
                memoryLockManager.release(DEFAULT_KEY);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                memoryLockManager.acquire(DEFAULT_KEY);
                counter.increase();
                memoryLockManager.release(DEFAULT_KEY);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println(counter.getCount());
        assertThat(counter.getCount()).isEqualTo(3 * numberOfIteration);
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

        int numberOfIteration = 1000000;

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                lockManager.acquire(DEFAULT_KEY);
                counter.increase();
                lockManager.release(DEFAULT_KEY);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                lockManager.acquire(DEFAULT_KEY);
                counter.increase();
                lockManager.release(DEFAULT_KEY);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < numberOfIteration; i++) {
                lockManager.acquire(DEFAULT_KEY);
                counter.increase();
                lockManager.release(DEFAULT_KEY);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        assertThat(lockManager.getSetCallCount()).isEqualTo(3 * numberOfIteration);
        assertThat(lockManager.getReleaseCallCount()).isEqualTo(3 * numberOfIteration);
    }
}
