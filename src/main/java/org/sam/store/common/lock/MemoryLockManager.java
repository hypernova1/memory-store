package org.sam.store.common.lock;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Component
public class MemoryLockManager implements LockManager {

    private static final long DEFAULT_ADDITIONAL_MILLISECONDS = 1000 * 60L;
    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    /**
     * 락을 획득한다.
     * - 같은 키로 이미 락이 있다면 해제될 때까지 대기한다.
     *
     * @param key 락 키
     */
    @Override
    public void acquire(String key) {
        long startTime = System.currentTimeMillis();
        long timeout = 1000 * 30;
        long elapsedTime = 0;

        synchronized (locks) {
            while (exists(key)) {
                try {
                    long waitTime = timeout - elapsedTime;
                    if (waitTime <= 0) {
                        throw new TimeoutException("락 획득 실패. key: " + key);
                    }
                    locks.wait(waitTime);
                    elapsedTime = System.currentTimeMillis() - startTime;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
            set(key);
        }

    }

    /**
     * 락을 저장한다.
     *
     * @param key 락 키
     */
    @Override
    public void set(String key) {
        this.set(key, DEFAULT_ADDITIONAL_MILLISECONDS);
    }

    /**
     * 락을 저장한다.
     *
     * @param key                    락 키
     * @param additionalMilliSeconds 락 만료 시간 ms
     */
    public synchronized void set(String key, long additionalMilliSeconds) {
        if (exists(key)) {
            throw new AlreadyLockException();
        }
        Lock lock = new Lock(key, additionalMilliSeconds);
        locks.put(key, lock);
    }

    /**
     * 락을 해제한다.
     *
     * @param key 락 키
     */
    @Override
    public void release(String key) {
        synchronized (locks) {
            locks.remove(key);
            locks.notifyAll();
        }
    }

    /**
     * 락 존재 여부를 확인한다.
     *
     * @param key 락 키
     * @return 락 존재 여부
     */
    @Override
    public synchronized boolean exists(String key) {
        Optional<Lock> lock = this.findLock(key);
        if (lock.isEmpty()) {
            return false;
        }
        if (lock.get().isExpired()) {
            release(key);
            return false;
        }
        return true;
    }

    /**
     * 락을 찾는다.
     *
     * @param key 락 키
     * @return 락
     */
    private Optional<Lock> findLock(String key) {
        Lock value = this.locks.get(key);
        return Optional.ofNullable(value);
    }

}
