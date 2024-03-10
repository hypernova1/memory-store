package org.sam.store.product;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.ProductNotFoundException;
import org.sam.store.common.lock.LockManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    public List<Product> decreaseProductsQuantity(List<ProductQuantityInfo> productQuantityInfos) {
        String lockKey = "product_quantity";
        lockManager.setOrWait(lockKey);

        List<String> productIds = productQuantityInfos.stream().map(ProductQuantityInfo::getProductId).collect(Collectors.toCollection(ArrayList::new));
        List<Product> products = this.productRepository.findByIds(productIds);

        products.forEach((product) -> {
            int quantity = productQuantityInfos.stream().filter((pq) -> pq.getProductId().equals(product.getId())).findFirst().get().getQuantity();
            product.decreaseQuantity(quantity);
        });

        lockManager.release(lockKey);
        return products;
    }

    @Transactional
    public List<Product> increaseProductsQuantity(List<ProductQuantityInfo> productQuantityInfos) {
        String lockKey = "product_quantity";
        lockManager.setOrWait(lockKey);

        List<String> productIds = productQuantityInfos.stream().map(ProductQuantityInfo::getProductId).collect(Collectors.toCollection(ArrayList::new));
        List<Product> products = this.productRepository.findByIds(productIds);

        products.forEach((product) -> {
            int quantity = productQuantityInfos.stream().filter((pq) -> pq.getProductId().equals(product.getId())).findFirst().get().getQuantity();
            product.increaseQuantity(quantity);
        });

        lockManager.release(lockKey);
        return products;
    }

}
