package org.sam.store.common.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MemoryInstanceUtil {

    protected static <T> boolean isEntity(T t) {
        Annotation annotation = t.getClass().getAnnotation(Entity.class);
        return annotation != null;
    }

    @SuppressWarnings("unchecked")
    protected static  <T, U> U getId(T t) {
        try {
            return (U) MemoryInstanceUtil.getFieldValue(t, Id.class);
        } catch (ClassCastException e) {
            throw new IdTypeNotMatchException();
        }
    }

    protected static <T> void setUpdatedAt(T t, LocalDateTime now) {
        Field lastMofifiedDateField = getPropertyField(t, LastModifiedDate.class);
        if (lastMofifiedDateField != null) {
            setFieldValue(t, now, LastModifiedDate.class);
        }
    }

    protected static <T> void setCreatedAt(T t, LocalDateTime now) {
        Field lastMofifiedDateField = getPropertyField(t, CreatedDate.class);
        if (lastMofifiedDateField != null) {
            setFieldValue(t, now, CreatedDate.class);
        }
    }

    private static <T> void setFieldValue(T t, Object value, Class<? extends Annotation> annotationClass) {
        Field field = getPropertyField(t, annotationClass);
        if (field == null) {
            throw new EntityPropertyNotFoundException();
        }

        field.setAccessible(true);

        try {
            field.set(t, value);
        } catch (IllegalAccessException | EntityPropertyNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Object getFieldValue(T t, Class<? extends Annotation> annotationClass) {
        Field field = getPropertyField(t, annotationClass);
        if (field == null) {
            throw new EntityPropertyNotFoundException();
        }

        field.setAccessible(true);

        try {
            return field.get(t);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Field getPropertyField(T t, Class<? extends Annotation> annotationClass) {
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
