package org.sam.store.common.lock;

import org.assertj.core.api.LocalDateTimeAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemoryLockManagerTest {

    @Autowired
    private MemoryLockManager memoryLockManager;

    @AfterEach
    void release() {
        if (memoryLockManager.exists("hello")) {
            memoryLockManager.release("hello");
        }
    }

    @Test
    void test_denied_duplication_key() {
        String key = "hello";
        memoryLockManager.set(key);
        assertThatExceptionOfType(AlreadyLockException.class)
                .isThrownBy(() -> memoryLockManager.set("hello"));
    }

    @Test
    void test_release() {
        String key = "hello";

        memoryLockManager.set(key);
        memoryLockManager.release(key);

        assertDoesNotThrow(() -> memoryLockManager.set(key));
    }

    @Test
    void test_extend_time() {
        String key = "hello";

        memoryLockManager.set(key);
        Lock lock = memoryLockManager.get(key);
        LocalDateTime beforeExpiredTime = lock.getExpiredTime();

        memoryLockManager.extendsTime(key, 1000 * 60);
        LocalDateTime afterExpiredTime = lock.getExpiredTime();

        assertThat(beforeExpiredTime.isBefore(afterExpiredTime)).isTrue();
    }

    @Test
    void test_set_or_wait() {

    }

}