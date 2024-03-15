package org.sam.store.common.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DefaultMemoryRepositoryTest {

    private final DefaultMemoryRepository<DummyItem, String> defaultMemoryRepository = new DefaultMemoryRepository<>() {};

    @AfterEach
    void clearItems() {
        this.defaultMemoryRepository.items.clear();
    }

    @Test
    void not_exists_id() {
        DummyItem item = DummyItem.createEmptyInstance();

        assertThatExceptionOfType(IdNotExistException.class)
                .isThrownBy(() -> defaultMemoryRepository.save(item));
    }

    @Test
    void save() {
        DummyItem item = DummyItem.create();
        defaultMemoryRepository.save(item);
        assertThat(defaultMemoryRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void save_all() {
        int size = 10;
        List<DummyItem> dummyItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DummyItem item = DummyItem.create();
            dummyItems.add(item);
        }

        defaultMemoryRepository.save(dummyItems);
        assertThat(defaultMemoryRepository.findAll().size()).isEqualTo(size);
    }

    @Test
    void find_by_id() {
        DummyItem item = DummyItem.createEmptyInstance();
        String id = UUID.randomUUID().toString();
        item.setId(id);
        defaultMemoryRepository.save(item);

        assertThat(defaultMemoryRepository.findById(id).isPresent()).isTrue();
        assertThat(defaultMemoryRepository.findById(id).get()).isEqualTo(item);

    }

}