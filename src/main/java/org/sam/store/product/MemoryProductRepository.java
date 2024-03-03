package org.sam.store.product;

import org.sam.store.common.util.CsvReader;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryProductRepository implements ProductRepository {

    private final List<Product> items;

    {
        this.items = CsvReader.read("test_data.csv");
    }

    @Override
    public List<Product> findAll() {
        return this.items;
    }

    @Override
    public Optional<Product> findOne(String id) {
        return items.stream().filter((item) -> item.getId().equals(id)).findFirst();
    }
}
