package com.unimagdalena.order_service.controller;


import com.unimagdalena.order_service.Dto.OrderServiceDto;
import com.unimagdalena.order_service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderServiceDto createOrder(@RequestParam String productId, @RequestParam int quantity) {
        return orderService.createOrder(productId, quantity);
    }
}
