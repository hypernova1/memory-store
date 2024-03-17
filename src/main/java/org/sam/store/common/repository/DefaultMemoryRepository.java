package org.sam.store.common.repository;

import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DefaultMemoryRepository<T, U> implements Repository<T, U> {
    protected final List<T> items = new ArrayList<>();

    @Override
    public T save(T t) {
        LocalDateTime now = LocalDateTime.now();
        U inputItemId = this.getId(t);
        if (inputItemId == null) {
            throw new IdNotExistException();
        }
        for (int i = 0; i < this.items.size(); i++) {
            U itemId = this.getId(items.get(i));
            if (itemId.equals(inputItemId)) {
                this.setUpdatedAt(t, now);
                this.items.set(i, t);
                return t;
            }
        }

        this.setUpdatedAt(t, now);
        this.setCreatedAt(t, now);
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
            U itemId = this.getId(item);
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

    protected U getId(T t) {
        return (U) getFieldValueByAnnotationType(t, Id.class);
    }

    private void setUpdatedAt(T t, LocalDateTime now) {
        Field lastMofifiedDateField = this.getPropertyField(t, LastModifiedDate.class);
        if (lastMofifiedDateField != null) {
            this.setValue(t, now, LastModifiedDate.class);
        }
    }

    private void setCreatedAt(T t, LocalDateTime now) {
        Field lastMofifiedDateField = this.getPropertyField(t, CreatedDate.class);
        if (lastMofifiedDateField != null) {
            this.setValue(t, now, CreatedDate.class);
        }
    }

    protected void setValue(T t, Object value, Class<? extends Annotation> annotationClass) {
        Field field = this.getPropertyField(t, annotationClass);
        field.setAccessible(true);
        try {
            field.set(t, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValueByAnnotationType(T t, Class<? extends Annotation> annotationClass) {
        Field field = this.getPropertyField(t, annotationClass);
        try {
            field.setAccessible(true);
            return field.get(t);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getPropertyField(T t, Class<? extends Annotation> annotationClass) {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                Annotation annotation = field.getAnnotation(annotationClass);
                if (annotation == null) {
                    continue;
                }

                field.setAccessible(true);
                return field;
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}