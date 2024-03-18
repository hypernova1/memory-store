package org.sam.store.order;

import org.sam.store.common.repository.DefaultMemoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemoryOrderRepository extends DefaultMemoryRepository<Order, String> implements OrderRepository {}
