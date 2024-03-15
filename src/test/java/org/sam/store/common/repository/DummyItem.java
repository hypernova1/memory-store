package org.sam.store.common.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class DummyItem {

    private String value;
    @Id
    private String id;

    protected DummyItem() {}

    public static DummyItem createEmptyInstance() {
        return new DummyItem();
    }

    public static DummyItem create() {
        DummyItem dummyItem = new DummyItem();
        dummyItem.id = UUID.randomUUID().toString();
        return dummyItem;
    }

    public void setId(String id) {
        this.id = id;
    }

}
