package org.sam.store.product.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductQuantityInfo {
    private String productId;
    private int quantity;
}
