package org.sam.store.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public void order(@RequestBody OrderForm orderForm) {
        this.orderService.order(orderForm);
    }

    @PatchMapping("/{id}/cancel")
    public void cancel(@PathVariable String id) {
        this.orderService.cancel(id);
    }

}
