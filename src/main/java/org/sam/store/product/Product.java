package org.sam.store.product;

import lombok.Getter;
import lombok.ToString;
import org.sam.store.common.BaseEntity;
import org.sam.store.common.exception.SoldOutException;
import org.sam.store.common.repository.annotation.Column;
import org.sam.store.common.repository.annotation.Entity;
import org.sam.store.common.repository.annotation.Id;

import java.util.UUID;

@Entity
@Getter
@ToString
public class Product extends BaseEntity {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private double price = 0;

    @Column
    private int quantity;

    public Product() {
        this.id = UUID.randomUUID().toString();
    }

    public Product(int quantity) {
        this();
        this.quantity = quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new SoldOutException();
        }
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
