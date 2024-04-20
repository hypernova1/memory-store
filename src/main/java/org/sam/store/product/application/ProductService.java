package org.sam.store.product.application;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.ProductNotFoundException;
import org.sam.store.common.lock.LockManager;
import org.sam.store.product.domain.Product;
import org.sam.store.product.domain.ProductRepository;
import org.sam.store.product.ui.ProductQuantityInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LockManager lockManager;

    public List<Product> getProducts(int page, int size) {
        return this.productRepository.find(page, size);
    }

    public Product getProduct(String id) {
        return this.productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<Product> decreaseProductsQuantity(List<ProductQuantityInfo> productQuantityInfos) {
        List<String> productIds = productQuantityInfos.stream().map(ProductQuantityInfo::getProductId).collect(Collectors.toCollection(ArrayList::new));
        List<String> lockKeys = this.acquireQuantityLocks(productIds);

        try {

            List<Product> products = this.productRepository.findByIds(productIds);

            products.forEach((product) -> {
                int quantity = productQuantityInfos.stream().filter((pq) -> pq.getProductId().equals(product.getId())).findFirst().get().getQuantity();
                product.decreaseQuantity(quantity);
            });
            return products;
        } finally {
            this.releaseQuantityLocks(lockKeys);
        }
    }

    public List<Product> increaseProductsQuantity(List<ProductQuantityInfo> productQuantityInfos) {
        List<String> productIds = productQuantityInfos.stream().map(ProductQuantityInfo::getProductId).collect(Collectors.toCollection(ArrayList::new));
        List<String> lockKeys = this.acquireQuantityLocks(productIds);
        try {

            List<Product> products = this.productRepository.findByIds(productIds);
            products.forEach((product) -> {
                int quantity = productQuantityInfos.stream().filter((pq) -> pq.getProductId().equals(product.getId())).findFirst().get().getQuantity();
                product.increaseQuantity(quantity);
            });
            return products;

        } finally {
            this.releaseQuantityLocks(lockKeys);
        }
    }

    private List<String> acquireQuantityLocks(List<String> ids) {
        List<String> lockKeys = new ArrayList<>();
        for (String id : ids) {
            String lockKey = "product_quantity_" + id;
            lockManager.acquire(lockKey);
            lockKeys.add(lockKey);
        }
        return lockKeys;
    }

    private void releaseQuantityLocks(List<String> lockKeys) {
        for (String lockKey : lockKeys) {
            lockManager.release(lockKey);
        }
    }

}
