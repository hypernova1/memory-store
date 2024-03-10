package org.sam.store.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    void saveAll(List<Product> products);
    List<Product> findAll();
    Optional<Product> findOne(String id);
    List<Product> findByIds(List<String> productIds);
}
