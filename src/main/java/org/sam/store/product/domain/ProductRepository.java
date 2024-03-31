package org.sam.store.product.domain;

import org.sam.store.common.repository.Repository;

import java.util.List;

public interface ProductRepository extends Repository<Product, String> {
    List<Product> findByIds(List<String> productIds);
}
