package org.sam.store.order.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sam.store.common.repository.annotation.Column;
import org.sam.store.common.repository.annotation.Entity;
import org.sam.store.common.repository.annotation.Id;
import org.sam.store.product.domain.Product;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    private Long id;

    private Product product;

    @Column
    private int quantity;

    public static OrderProduct of(Product product, int quantity) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.product = product;
        orderProduct.quantity = quantity;
        return orderProduct;
    }

    public double price() {
        return this.product.getPrice() * this.quantity;
    }
}
