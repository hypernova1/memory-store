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
    protected static final String LOCK_KEY = "melchor";

    @Autowired
    protected MemoryLockManager memoryLockManager;

    @AfterEach
    void release() {
        if (memoryLockManager.exists(LOCK_KEY)) {
            memoryLockManager.release(LOCK_KEY);
        }
    }

    @Test
    void test_denied_duplication_key() {
        memoryLockManager.set(LOCK_KEY);
        assertThatExceptionOfType(AlreadyLockException.class)
                .isThrownBy(() -> memoryLockManager.set(LOCK_KEY));
    }

    @Test
    void test_release() {
        memoryLockManager.set(LOCK_KEY);
        memoryLockManager.release(LOCK_KEY);

        assertDoesNotThrow(() -> memoryLockManager.set(LOCK_KEY));
    }

    @Test
    void test_extend_time() {
        memoryLockManager.set(LOCK_KEY);
        Lock lock = memoryLockManager.get(LOCK_KEY);
        LocalDateTime beforeExpiredTime = lock.getExpiredTime();

        memoryLockManager.extendsTime(LOCK_KEY, 1000 * 60);
        LocalDateTime afterExpiredTime = lock.getExpiredTime();

        assertThat(beforeExpiredTime.isBefore(afterExpiredTime)).isTrue();
    }

}