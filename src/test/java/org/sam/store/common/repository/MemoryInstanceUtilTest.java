package org.sam.store.common.repository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemoryInstanceUtilTest {
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