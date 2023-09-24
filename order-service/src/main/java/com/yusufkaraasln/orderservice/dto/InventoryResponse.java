package com.yusufkaraasln.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private String skuCode;
    private boolean isInStock;



}
