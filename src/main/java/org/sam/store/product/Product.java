package org.sam.store.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.sam.store.common.BaseEntity;
import org.sam.store.common.exception.SoldOutException;

@Entity
@Getter
@ToString
public class Product extends BaseEntity {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private double price;

    @Column
    private int quantity;

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
