package org.sam.store.common.lock;

public interface LockManager {
    void set(String id);
    void release(String id);
    void acquire(String id);
    boolean exists(String id);
    void extendsTime(String id, long time);
}
