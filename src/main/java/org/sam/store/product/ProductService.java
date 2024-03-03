package org.sam.store.product;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(int page, int size) {
        return this.productRepository.findAll();
    }

    public Product getProduct(String id) {
        return this.productRepository.findOne(id)
                .orElseThrow(ProductNotFoundException::new);
    }

}
