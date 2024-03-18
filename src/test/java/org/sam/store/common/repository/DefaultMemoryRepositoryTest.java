package org.sam.store.common.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.sam.store.common.repository.exception.IdNotExistException;
import org.sam.store.order.OrderRepository;
import org.sam.store.product.Product;
import org.sam.store.product.ProductRepository;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DefaultMemoryRepositoryTest {

    private final DefaultMemoryRepository<DummyItem, String> defaultMemoryRepository = new DefaultMemoryRepository<>() {};

    @AfterEach
    void clearItems() {
        this.defaultMemoryRepository.items.clear();
    }

    @Test
    void not_exists_id() {
        DummyItem item = DummyItem.createEmptyInstance();

        assertThatExceptionOfType(IdNotExistException.class)
                .isThrownBy(() -> defaultMemoryRepository.save(item));
    }

    @Test
    void save() {
        DummyItem item = DummyItem.create();
        defaultMemoryRepository.save(item);
        assertThat(defaultMemoryRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void save_all() {
        int size = 10;
        List<DummyItem> dummyItems = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DummyItem item = DummyItem.create();
            dummyItems.add(item);
        }

        defaultMemoryRepository.save(dummyItems);
        assertThat(defaultMemoryRepository.findAll().size()).isEqualTo(size);
    }

    @Test
    void find_by_id() {
        DummyItem item = DummyItem.createEmptyInstance();
        String id = UUID.randomUUID().toString();
        item.setId(id);
        defaultMemoryRepository.save(item);

        assertThat(defaultMemoryRepository.findById(id).isPresent()).isTrue();
        assertThat(defaultMemoryRepository.findById(id).get()).isEqualTo(item);
    }

    @Test
    void proxy_instance() {
        ProxyRepository<Product, String> defaultMemoryRepository = new ProxyRepository<>();
        ProductRepository productRepository = (ProductRepository) Proxy.newProxyInstance(
                Repository.class.getClassLoader(),
                new Class[] {ProductRepository.class},
                defaultMemoryRepository
        );

        ProxyRepository<Order, String> defaultMemoryRepository2 = new ProxyRepository<>();
        OrderRepository orderRepository = (OrderRepository) Proxy.newProxyInstance(
                Repository.class.getClassLoader(),
                new Class[] {OrderRepository.class},
                defaultMemoryRepository2
        );

        Product product = new Product();
        productRepository.save(product);

        assertThat(productRepository.findAll().size()).isEqualTo(1);
        assertThat(orderRepository.findAll().size()).isEqualTo(0);

        System.out.println(productRepository.findByIds(new ArrayList<>()));
    }

}