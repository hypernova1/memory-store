package org.sam.store.common.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemoryLockManagerTest {
    private static final String DEFAULT_KEY = "melchor";

    @Autowired
    private MemoryLockManager memoryLockManager;

    @AfterEach
    void release() {
        if (memoryLockManager.exists(DEFAULT_KEY)) {
            memoryLockManager.release(DEFAULT_KEY);
        }
    }

    @Test
    void test_denied_duplication_key() {
        memoryLockManager.set(DEFAULT_KEY);
        assertThatExceptionOfType(AlreadyLockException.class)
                .isThrownBy(() -> memoryLockManager.set(DEFAULT_KEY));
    }

    @Test
    void test_release() {
        memoryLockManager.set(DEFAULT_KEY);
        memoryLockManager.release(DEFAULT_KEY);

        assertDoesNotThrow(() -> memoryLockManager.set(DEFAULT_KEY));
    }

    @Test
    void test_extend_time() {
        memoryLockManager.set(DEFAULT_KEY);
        Lock lock = memoryLockManager.get(DEFAULT_KEY);
        LocalDateTime beforeExpiredTime = lock.getExpiredTime();

        memoryLockManager.extendsTime(DEFAULT_KEY, 1000 * 60);
        LocalDateTime afterExpiredTime = lock.getExpiredTime();

        assertThat(beforeExpiredTime.isBefore(afterExpiredTime)).isTrue();
    }

    private static int value = 0;
    private static final int iterateCount = 999;

    @Test
    void concurrency_test() throws InterruptedException {
        int numThreads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            executorService.execute(() -> {
                String threadName = "thread-" + finalI;
                System.out.println("================== start thread - " + threadName);
                memoryLockManager.acquire("lock");
                System.out.println("start func - " + threadName);
                func();
                System.out.println("finish func - " + threadName);
                memoryLockManager.release("lock");
                latch.countDown();
                System.out.println("=================== finish " + threadName);
            });
        }

        latch.await();
        assertThat(value).isEqualTo(numThreads * iterateCount);
    }

    void func() {
        for (int i = 0; i < iterateCount; i++) {
            value++;
        }
    }

}