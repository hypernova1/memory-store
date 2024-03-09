package org.sam.store.common.lock;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class MemoryLockManager implements LockManager {

    private static final long DEFAULT_ADDITIONAL_NANO_TIME = 1000 * 60L;
    private final List<Lock> locks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void set(String id) {
        this.set(id, DEFAULT_ADDITIONAL_NANO_TIME);
    }

    public void set(String id, long additionalNanoTime) {
        if (existsLock(id)) {
            throw new RuntimeException();
        }

        Lock lock = new Lock(id, additionalNanoTime);
        this.locks.add(lock);
    }

    @Override
    public void release(String id) {
        Lock lock = findLock(id)
                .orElseThrow(RuntimeException::new);

        this.locks.remove(lock);
    }

    @Override
    public void extendsTime(String id, long nanoTime) {
        Lock lock = this.findLock(id)
                .orElseThrow(RuntimeException::new);
        lock.extendExpiredTime(nanoTime);
    }

    public Lock get(String id) {
        return this.findLock(id).orElseThrow(RuntimeException::new);
    }

    private Optional<Lock> findLock(String id) {
        return this.locks.stream().filter((l) -> l.getId().equals(id)).findFirst();
    }

    private boolean existsLock(String id) {
        return this.locks.stream().anyMatch((lock) -> lock.getId().equals(id));
    }
}
