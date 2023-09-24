package com.yusufkaraasln.orderservice.service;


import com.yusufkaraasln.orderservice.dto.InventoryResponse;
import com.yusufkaraasln.orderservice.event.OrderPlacedEvent;
import com.yusufkaraasln.orderservice.repository.OrderRepository;
import com.yusufkaraasln.orderservice.dto.OrderRequest;
import com.yusufkaraasln.orderservice.entity.Order;
import com.yusufkaraasln.orderservice.entity.OrderLineItems;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest) {

        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDTOList().stream().map(orderLineItemsDTO -> {
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setPrice(orderLineItemsDTO.getPrice());
            orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
            orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());


            return orderLineItems;
        }).toList();

        order.setOrderLineItems(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItems()
                .stream()
                .map(OrderLineItems::getSkuCode).toList();

        // call inventory service and check if the products are available

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(
                item -> item.isInStock() == true
        );


        if (allProductsInStock && inventoryResponses.length > 0) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",
                        new OrderPlacedEvent(order.getOrderNumber())
                    );
            return "Order placed successfully";
        } else {
            throw new IllegalArgumentException("Product is out of stock");
        }


    }


}
