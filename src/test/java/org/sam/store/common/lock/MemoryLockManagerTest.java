package org.sam.store.common.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemoryLockManagerTest {
    protected static final String DEFAULT_KEY = "melchor";

    @Autowired
    protected MemoryLockManager memoryLockManager;

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

}