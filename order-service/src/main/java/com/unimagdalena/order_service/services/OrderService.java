package com.unimagdalena.order_service.services;

import com.unimagdalena.order_service.Dto.OrderServiceDto;

public interface OrderService {

    OrderServiceDto createOrder (String productId, int quantity);

    OrderServiceDto getById(String id);

}
