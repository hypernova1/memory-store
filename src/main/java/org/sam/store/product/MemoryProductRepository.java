package org.sam.store.product;

import org.sam.store.common.repository.DefaultMemoryRepository;
import org.sam.store.common.util.CsvUtil;
import org.sam.store.order.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryProductRepository extends DefaultMemoryRepository<Product, String> implements ProductRepository {

    private final List<Product> items;

    {
        this.items = CsvUtil.createInstance("test_data.csv", Product.class);
    }

    @Override
    public List<Product> findByIds(List<String> productIds) {
        return this.items.stream().filter((product) -> productIds.contains(product.getId())).collect(Collectors.toCollection(ArrayList::new));
    }

}
