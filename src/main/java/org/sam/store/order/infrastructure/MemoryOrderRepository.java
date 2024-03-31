package org.sam.store.order.infrastructure;

import org.sam.store.common.repository.DefaultMemoryRepository;
import org.sam.store.order.domain.OrderRepository;
import org.sam.store.order.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryOrderRepository extends DefaultMemoryRepository<Order, String> implements OrderRepository {}
