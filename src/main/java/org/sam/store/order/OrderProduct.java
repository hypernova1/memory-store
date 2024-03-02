package org.sam.store.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sam.store.product.Product;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    private Long id;

    @OneToOne
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
