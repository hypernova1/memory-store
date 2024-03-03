package org.sam.store.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import org.sam.store.common.BaseEntity;
import org.sam.store.product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    private Long id;

    @OneToMany
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    private double shippingPrice = 0;

    public void addProduct(Product product, int quantity) {
        product.decreaseQuantity(quantity);
        this.orderProducts.add(OrderProduct.of(product, quantity));
    }

    public double totalProductPrice() {
        double[] prices = this.orderProducts.stream().mapToDouble(OrderProduct::price).toArray();
        return Arrays.stream(prices).sum();
    }
}
