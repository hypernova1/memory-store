package org.sam.store.order.domain;

public enum OrderStatus {
    PAYMENT_WAITING {
        @Override
        public boolean isShippingChangeable() {
            return true;
        }
    },

    SHIPPED, DELIVERING, DELIVERY_COMPLETE, CANCELLED;

    public boolean isShippingChangeable() {
        return false;
    }
}
