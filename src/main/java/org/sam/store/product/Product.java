package org.sam.store.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.sam.store.common.BaseEntity;

@Entity
public class Product extends BaseEntity {
    @Id
    private String id;

}
