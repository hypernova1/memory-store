package org.sam.store.common.lock;

public interface LockManager {
    void set(String id);
    void release(String id);
    void extendsTime(String id, long time);
}
