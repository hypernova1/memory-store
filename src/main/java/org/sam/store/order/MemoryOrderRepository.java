package org.sam.store.order;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        List<Long> orderIds = this.orders.stream().mapToLong(Order::getId).boxed().collect(Collectors.toCollection(ArrayList::new));
        int targetOrderId = orderIds.indexOf(order.getId());
        if (targetOrderId == -1) {
            this.orders.add(order);
            return;
        }
        this.orders.set(targetOrderId, order);
    }

    @Override
    public Optional<Order> findOne(Long id) {
        return this.orders.stream().filter((order) -> order.getId().equals(id)).findFirst();
    }

}
