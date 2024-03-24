package org.sam.store.common.repository;

public class Counter {

    private int count = 0;

    public void increase() {
        this.count++;
    }

    public int getCount() {
        return this.count;
    }

}
