package org.sam.store.order;

public class Shipping {

    private final static double MIN_PRICE = 50_000;

    public static boolean isFreeShippingOrder(Order order) {
        return MIN_PRICE <= order.totalProductPrice();
    }

}
