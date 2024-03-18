package org.sam.store.common.repository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemoryInstanceUtilTest {

    @Test
    void is_entity() {
        DummyItem entityItem = new DummyItem();
        Object obj = "No Entity!!";

        assertThat(MemoryInstanceUtil.isEntity(entityItem)).isTrue();
        assertThat(MemoryInstanceUtil.isEntity(obj)).isFalse();
    }

    @Test
    void find_id() {
        DummyItem item = DummyItem.createEmptyInstance();
        String id = UUID.randomUUID().toString();
        item.setId(id);
        String idInItem = MemoryInstanceUtil.getId(item);
        assertThat(idInItem).isEqualTo(id);
    }
    @Test
    void test_created_at_and_updated_at() {
        LocalDateTime now = LocalDateTime.now();

        DummyItem item = DummyItem.create();
        MemoryInstanceUtil.setUpdatedAt(item, now);
        MemoryInstanceUtil.setCreatedAt(item, now);

        assertThat(item.getCreatedAt()).isEqualTo(now);
        assertThat(item.getUpdatedAt()).isEqualTo(now);
    }

}