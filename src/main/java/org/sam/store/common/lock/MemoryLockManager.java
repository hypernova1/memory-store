package org.sam.store.common.lock;


import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoryLockManager implements LockManager {

    private static final long DEFAULT_ADDITIONAL_NANO_TIME = 1000 * 60L;
    private static final int MAX_WAITING_TIME = 1000 * 60;
    private final List<Lock> locks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void acquire(String id) {
        try {
            int waitTime = 0;
            while (waitTime < MAX_WAITING_TIME) {
                if (!exists(id)) {
                    set(id);
                    return;
                }
                Thread.sleep(10);
                waitTime += 10;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String id) {
        this.set(id, DEFAULT_ADDITIONAL_NANO_TIME);
    }

    public void set(String id, long additionalNanoTime) {
        if (exists(id)) {
            throw new AlreadyLockException();
        }

        Lock lock = new Lock(id, additionalNanoTime);
        this.locks.add(lock);
    }

    @Override
    public void release(String id) {
        Lock lock = findLock(id)
                .orElseThrow(NoLockException::new);

        this.locks.remove(lock);
    }

    @Override
    public void extendsTime(String id, long nanoTime) {
        Lock lock = this.findLock(id)
                .orElseThrow(NoLockException::new);
        lock.extendExpiredTime(nanoTime);
    }

    @Override
    public boolean exists(String id) {
        Optional<Lock> lock = this.locks.stream().filter((l) -> l.getId().equals(id)).findFirst();
        if (lock.isEmpty()) {
            return false;
        }
        if (lock.get().isExpired()) {
            release(id);
            return false;
        }
        return true;
    }

    public Lock get(String id) {
        return this.findLock(id).orElseThrow(NoLockException::new);
    }

    private Optional<Lock> findLock(String id) {
        return this.locks.stream().filter((l) -> l.getId().equals(id)).findFirst();
    }

}
