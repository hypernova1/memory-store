package org.sam.store.order;

import lombok.AllArgsConstructor;
import org.sam.store.common.exception.OrderNotFoundException;
import org.sam.store.product.Product;
import org.sam.store.product.ProductQuantityInfo;
import org.sam.store.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void cancel(Long id) {
        Order order = this.orderRepository.findOne(id)
                .orElseThrow(OrderNotFoundException::new);
        order.cancel();

        List<ProductQuantityInfo> productQuantityInfos = order.getOrderProducts().stream()
                .map((orderProduct) -> new ProductQuantityInfo(orderProduct.getProduct().getId(), orderProduct.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));

        this.productService.increaseProductsQuantity(productQuantityInfos);
    }


}
