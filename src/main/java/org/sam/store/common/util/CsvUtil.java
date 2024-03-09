package org.sam.store.common.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV 파일의 데이터를 클래스 인스턴스 목록으로 변환한다.
 * */
public class CsvUtil {

    public static <T> List<T> createInstance(String filePath, Class<T> _clazz) {
        ClassPathResource resource = new ClassPathResource(filePath);
        try {
            Path path = Paths.get(resource.getURI());
            List<String> contentsList = Files.readAllLines(path);
            String[] csvFieldNames = contentsList.getFirst().split(",");

            List<T> result = new ArrayList<>();
            for (int i = 1; i < contentsList.size(); i++) {
                String[] contents = contentsList.get(i).split(",");
                result.add(createNewInstance(_clazz, csvFieldNames, contents));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T createNewInstance(Class<T> _clazz, String[] csvFieldNames, String[] contents) {
        try {
            T instance = _clazz.newInstance();
            Class<?> clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                setField(csvFieldNames, contents, field, instance);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static <T> void setField(String[] csvFieldNames, String[] contents, Field field, T instance) throws IllegalAccessException {
        String fieldName = field.getName();

        int fieldIndex = Arrays.asList(csvFieldNames).indexOf(fieldName);
        if (fieldIndex == -1) {
            return;
        }
        Class<?> type = field.getType();
        String typeName = type.getTypeName();
        Object content = contents[fieldIndex];

        if (type.isPrimitive()) {
            content = parsePrimitiveValue(typeName, content);
        }

        field.setAccessible(true);
        field.set(instance, content);
    }

    private static Object parsePrimitiveValue(String typeName, Object value) {
        try {
            String primitiveTypeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
            String methodName = "parse" + primitiveTypeName;
            if (primitiveTypeName.equals("Int")) {
                primitiveTypeName = "Integer";
                methodName = "parseInt";
            }
            Class<?> boxTypeClass = Class.forName("java.lang." + primitiveTypeName);
            Method method = boxTypeClass.getMethod(methodName, String.class);
            value = method.invoke(null, value);
            return value;
        } catch (IllegalAccessException |
                 ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

}
