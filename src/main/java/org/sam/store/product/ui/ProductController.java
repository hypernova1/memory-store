package org.sam.store.product.ui;

import lombok.AllArgsConstructor;
import org.sam.store.product.domain.Product;
import org.sam.store.product.application.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return this.productService.getProducts(page, size);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        Product product = this.productService.getProduct(id);
        return product;
    }

}
