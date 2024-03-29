package org.sam.store.common.repository;

import org.sam.store.common.repository.exception.IdNotExistException;
import org.sam.store.common.repository.exception.NoEntityException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultMemoryRepository<T, U> implements Repository<T, U> {
    protected final List<T> items = new ArrayList<>();

    @Override
    public T save(T t) {
        if (!MemoryInstanceUtil.isEntity(t)) {
            throw new NoEntityException();
        }

        LocalDateTime now = LocalDateTime.now();
        U inputItemId = MemoryInstanceUtil.getId(t);
        if (inputItemId == null) {
            throw new IdNotExistException();
        }

        for (int i = 0; i < this.items.size(); i++) {
            U itemId = MemoryInstanceUtil.getId(items.get(i));
            if (itemId.equals(inputItemId)) {
                MemoryInstanceUtil.setUpdatedAt(t, now);
                this.items.set(i, t);
                return t;
            }
        }

        MemoryInstanceUtil.setUpdatedAt(t, now);
        MemoryInstanceUtil.setCreatedAt(t, now);
        this.items.add(t);
        return t;
    }

    @Override
    public List<T> save(List<T> items) {
        return items.stream().map(this::save).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<T> findById(U id) {
        for (T item : this.items) {
            U itemId = MemoryInstanceUtil.getId(item);
            if (itemId.equals(id)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return items;
    }

    @Override
    public List<T> find(int page, int size) {
        int first = (page - 1) * size;
        int last = first + size;

        if (first > items.size()) {
            return Collections.emptyList();
        }

        if (last > items.size()) {
            last = items.size();
        }
        return this.items.subList(first, last);
    }

    @Override
    public void delete(T t) {
        this.items.remove(t);
    }

    @Override
    public void delete(List<T> list) {
        this.items.removeAll(list);
    }

    @Override
    public void deleteById(U id) {
        T item = this.findById(id).orElse(null);
        if (item == null) {
            return;
        }
        this.items.remove(item);
    }

}