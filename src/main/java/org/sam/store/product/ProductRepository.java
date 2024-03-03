package org.sam.store.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findOne(String id);
    List<Product> findByIds(List<String> productIds);
}
