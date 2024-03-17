package org.sam.store.payment;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sam.store.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    private String id;

    @Column
    private double totalAmount;

    @Column
    private double productPrice;

    @Column
    private double shippingFee;

    @Column
    private double discountAmount;

}
