package org.sam.store.product;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.ProductNotFoundException;
import org.sam.store.common.lock.LockManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LockManager lockManager;

    public List<Product> getProducts(int page, int size) {
        return this.productRepository.findAll();
    }

    public Product getProduct(String id) {
        return this.productRepository.findOne(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<Product> findByIds(List<String> productIds) {
        return this.productRepository.findByIds(productIds);
    }

    public void decreaseProductsQuantity() {
        lockManager.setOrWait("decrease_product_quantity");
    }
}
