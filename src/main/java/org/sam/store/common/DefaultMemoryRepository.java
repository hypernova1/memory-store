
import jakarta.persistence.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public abstract class DefaultMemoryRepository<T, U> {
    protected List<T> items;

    public void save(T t) {
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Field idField = null;
        for (Field field : fields) {
            try {
                Annotation annotation = field.getAnnotation(Id.class);

                idField = field;

                for (int i = 0; i < items.size(); i++) {
                    Object value = idField.get(items.get(i));
                    Object value2 = idField.get(t);
                    if (value.equals(value2)) {
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

}