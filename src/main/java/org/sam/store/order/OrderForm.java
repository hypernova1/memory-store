package org.sam.store.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderProductDto> products;
}

@Data
class OrderProductDto {
    private String productId;
    private int quantity;
}
