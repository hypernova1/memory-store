package org.sam.store.common.constant;

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
