package com.unimagdalena.order_service.Dto;

import java.sql.Timestamp;

import com.unimagdalena.order_service.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrderServiceDto {
    private String id;
    private String productId;
    private Integer quantity;
    private Long totalAmount;
    private Status status;
    private Timestamp createAt;
}
