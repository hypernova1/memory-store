package org.sam.store.common.util;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sam.store.product.domain.Product;

import java.util.List;

class CsvUtilTest {

    @Test
    void get_resource() {
        List<Product> products = CsvUtil.createInstance("test_data.csv", Product.class);
        assertThat(products.size()).isNotZero();
    }

}