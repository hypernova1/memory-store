package org.sam.store.common.repository;


import org.sam.store.common.repository.annotation.CreatedDate;
import org.sam.store.common.repository.annotation.Entity;
import org.sam.store.common.repository.annotation.Id;
import org.sam.store.common.repository.annotation.UpdatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class DummyItem {

    private String value;
    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    @UpdatedDate
    private LocalDateTime updatedAt;

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

    public String getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
