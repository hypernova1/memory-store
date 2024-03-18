package org.sam.store.common.repository;

import org.sam.store.product.Product;
import org.sam.store.product.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class RepositoryBean {

    @Bean
    public ProductRepository orderRepository() {
        ProxyRepository<Product, String> defaultMemoryRepository = new ProxyRepository<>();
        ProductRepository productRepository = (ProductRepository) Proxy.newProxyInstance(
                Repository.class.getClassLoader(),
                new Class[] {ProductRepository.class},
                defaultMemoryRepository
        );

        return productRepository;
    }

}
