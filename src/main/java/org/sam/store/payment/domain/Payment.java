package org.sam.store.payment.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sam.store.common.BaseEntity;
import org.sam.store.common.repository.annotation.Column;
import org.sam.store.common.repository.annotation.Entity;
import org.sam.store.common.repository.annotation.Id;

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
