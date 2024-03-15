package org.sam.store.order;

import org.sam.store.common.repository.DefaultMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemoryOrderRepository extends DefaultMemoryRepository<Order, String> implements OrderRepository {

    @Override
    public Optional<Order> findOne(Long id) {
        return this.items.stream().filter((order) -> order.getId().equals(id)).findFirst();
    }

}
