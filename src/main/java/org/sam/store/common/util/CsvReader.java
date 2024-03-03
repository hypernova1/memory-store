package org.sam.store.common.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CsvReader {

    public static <T> List<T> read(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        try {
            Path path = Paths.get(resource.getURI());
//            List<String> content = Files.readAllLines(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Collections.emptyList();
    }

}
