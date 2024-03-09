package org.sam.store.order;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.BadOrderRequestException;
import org.sam.store.common.exception.OrderNotFoundException;
import org.sam.store.product.Product;
import org.sam.store.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public void order(OrderForm orderForm) {
        List<String> productIds = orderForm.getProducts()
                .stream()
                .map(OrderProductDto::getProductId)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        List<Product> products = productService.findByIds(productIds);

        if (products.size() != productIds.size()) {
            throw new BadOrderRequestException();
        }

        Order order = Order.create(orderForm, products);
        this.orderRepository.save(order);
    }

    public void cancel(Long id) {
        Order order = this.orderRepository.findOne(id)
                .orElseThrow(OrderNotFoundException::new);
        order.cancel();
        orderRepository.save(order);
    }


}
