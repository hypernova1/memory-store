package org.sam.store.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductQuantityInfo {
    private String productId;
    private int quantity;
}
