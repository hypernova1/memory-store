package org.sam.store.order;

import lombok.Getter;
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
        for (OrderProductDto orderProductDto : orderForm.getProducts()) {
            Product product = products.stream().filter((p) -> p.getId().equals(orderProductDto.getProductId())).findFirst()
                    .orElseThrow(ProductNotFoundException::new);
            order.addProduct(product, orderProductDto.getQuantity());
        }
        return order;
    }

    public void addProduct(Product product, int quantity) {
        this.orderProducts.add(OrderProduct.of(product, quantity));
    }

    public double totalProductPrice() {
        double[] prices = this.orderProducts.stream().mapToDouble(OrderProduct::price).toArray();
        return Arrays.stream(prices).sum();
    }

    public void cancel() {
        if (this.isCancellable()) {
            throw new RuntimeException();
        }
        this.status = OrderStatus.CANCELLED;
    }

    private boolean isCancellable() {
        return this.status.isShippingChangeable();
    }
}
