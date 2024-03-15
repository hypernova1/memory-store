package org.sam.store.order;

import org.sam.store.common.repository.DefaultMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryOrderRepository extends DefaultMemoryRepository<Order, String> implements OrderRepository {

    private final List<Order> items = new ArrayList<>();

    @Override
    public void save(Order order) {
        List<Long> orderIds = this.items.stream().mapToLong(Order::getId).boxed().collect(Collectors.toCollection(ArrayList::new));
        int targetOrderId = orderIds.indexOf(order.getId());
        if (targetOrderId == -1) {
            this.items.add(order);
            return;
        }
        this.items.set(targetOrderId, order);
    }

    @Override
    public Optional<Order> findOne(Long id) {
        return this.items.stream().filter((order) -> order.getId().equals(id)).findFirst();
    }

}
