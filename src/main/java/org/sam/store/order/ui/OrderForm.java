package org.sam.store.order.ui;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderProductDto> products;
}
