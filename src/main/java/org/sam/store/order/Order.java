package org.sam.store.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sam.store.common.BaseEntity;
import org.sam.store.common.constant.OrderStatus;
import org.sam.store.common.exception.ProductNotFoundException;
import org.sam.store.common.repository.annotation.Column;
import org.sam.store.common.repository.annotation.Entity;
import org.sam.store.common.repository.annotation.Id;
import org.sam.store.product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    private Long id;

    @Column
    private OrderStatus status;

    private final List<OrderProduct> orderProducts = new ArrayList<>();

    @Column
    private double shippingPrice = 0;

    public static Order create(OrderForm orderForm, List<Product> products) {
        Order order = new Order();
        order.addOrderProducts(orderForm, products);
        order.verifyOrderProducts();
        return order;
    }

    private void addOrderProducts(OrderForm orderForm, List<Product> products) {
        for (OrderProductDto orderProductDto : orderForm.getProducts()) {
            Product product = products.stream().filter((p) -> p.getId().equals(orderProductDto.getProductId())).findFirst()
                    .orElseThrow(ProductNotFoundException::new);
            this.orderProducts.add(OrderProduct.of(product, orderProductDto.getQuantity()));
        }
    }

    private void verifyOrderProducts() {
        if (this.orderProducts.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public double totalProductPrice() {
        double[] prices = this.orderProducts.stream().mapToDouble(OrderProduct::price).toArray();
        return Arrays.stream(prices).sum();
    }

    public void cancel() {
        if (this.status.isShippingChangeable()) {
            throw new RuntimeException();
        }
        this.status = OrderStatus.CANCELLED;
    }

}
