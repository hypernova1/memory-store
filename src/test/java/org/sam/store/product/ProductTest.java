package org.sam.store.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void test_decrease_quantity() {
        Product product = new Product();
        product.increaseQuantity(5);

        assertDoesNotThrow(() -> product.decreaseQuantity(5));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> {
                    product.decreaseQuantity(1);
                });
    }

}