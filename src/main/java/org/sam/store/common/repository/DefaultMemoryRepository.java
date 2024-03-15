package org.sam.store.common.repository;

import jakarta.persistence.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DefaultMemoryRepository<T, U> {
    protected final List<T> items = new ArrayList<>();

    public void save(T t) {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Field idField = null;
        for (Field field : fields) {
            try {
                Annotation annotation = field.getAnnotation(Id.class);
                if (annotation == null) {
                    continue;
                }

                idField = field;
                field.setAccessible(true);

                Object newItemId = idField.get(t);
                if (newItemId == null) {
                    throw new IdNotExistException();
                }

                for (int i = 0; i < items.size(); i++) {
                    Object value = idField.get(items.get(i));

                    if (value.equals(newItemId)) {
                        this.items.set(i, t);
                        return;
                    }

                }
            } catch (NullPointerException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        items.add(t);
    }

    public void save(List<T> items) {
        items.forEach(this::save);
    }

    public Optional<T> findById(U id) {
        T fistItem = this.items.getFirst();
        if (fistItem == null) {
            return Optional.empty();
        }

        Class<?> clazz = fistItem.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Field idField;
        for (Field field : fields) {
            try {
                Annotation annotation = field.getAnnotation(Id.class);
                if (annotation == null) {
                    continue;
                }

                idField = field;
                field.setAccessible(true);

                for (T item : items) {
                    Object value = idField.get(item);

                    if (value.equals(id)) {
                        return Optional.of(item);
                    }

                }
            } catch (NullPointerException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    public List<T> findAll() {
        return items;
    }

}