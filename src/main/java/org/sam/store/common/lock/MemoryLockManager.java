package org.sam.store.common.lock;


import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoryLockManager implements LockManager {

    private static final long DEFAULT_ADDITIONAL_NANO_TIME = 1000 * 60L;
    final List<Lock> locks = Collections.synchronizedList(new ArrayList<>());

    @Override
    public synchronized void acquire(String id) {
        while (exists(id)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        set(id);
        notifyAll();

    }

    @Override
    public void set(String id) {
        this.set(id, DEFAULT_ADDITIONAL_NANO_TIME);
    }

    public void set(String id, long additionalNanoTime) {
        synchronized (locks) {
            if (exists(id)) {
                throw new AlreadyLockException();
            }
            Lock lock = new Lock(id, additionalNanoTime);
            locks.add(lock);
        }
    }

    @Override
    public void release(String id) {
        synchronized (locks) {
            Iterator<Lock> iterator = locks.iterator();
            while (iterator.hasNext()) {
                Lock nextLock = iterator.next();
                if (nextLock.getId().equals(id)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void extendsTime(String id, long nanoTime) {
        Lock lock = this.findLock(id).orElseThrow(NoLockException::new);
        lock.extendExpiredTime(nanoTime);
    }

    @Override
    public boolean exists(String id) {
        synchronized (locks) {
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
    }

    private Optional<Lock> findLock(String id) {
        synchronized (locks) {
            return this.locks.stream().filter((l) -> l.getId().equals(id)).findFirst();
        }
    }

    synchronized public Lock get(String id) {
        return this.findLock(id).orElseThrow(NoLockException::new);
    }

}
