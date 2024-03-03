package org.sam.store.order;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);
    Optional<Order> findOne(Long id);

}
