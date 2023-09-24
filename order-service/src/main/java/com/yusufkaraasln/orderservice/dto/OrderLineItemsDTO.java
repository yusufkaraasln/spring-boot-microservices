package com.yusufkaraasln.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDTO {

    private Long id;

    private String skuCode;

    private Integer quantity;

    private Double price;
}
