package org.sam.store.order.application;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.OrderNotFoundException;
import org.sam.store.order.domain.Order;
import org.sam.store.order.domain.OrderRepository;
import org.sam.store.order.ui.OrderForm;
import org.sam.store.product.domain.Product;
import org.sam.store.product.ui.ProductQuantityInfo;
import org.sam.store.product.application.ProductService;
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
        List<ProductQuantityInfo> productQuantityInfos = orderForm.getProducts()
                .stream()
                .map((product) -> new ProductQuantityInfo(product.getProductId(), product.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));
        
        List<Product> products = productService.decreaseProductsQuantity(productQuantityInfos);
        Order order = Order.create(orderForm, products);
        this.orderRepository.save(order);
    }

    public void cancel(String id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        order.cancel();

        List<ProductQuantityInfo> productQuantityInfos = order.getOrderProducts().stream()
                .map((orderProduct) -> new ProductQuantityInfo(orderProduct.getProduct().getId(), orderProduct.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));

        this.productService.increaseProductsQuantity(productQuantityInfos);
    }


}
