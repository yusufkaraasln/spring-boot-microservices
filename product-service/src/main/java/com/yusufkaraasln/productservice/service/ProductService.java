package com.yusufkaraasln.productservice.service;


import com.yusufkaraasln.productservice.model.Product;
import com.yusufkaraasln.productservice.repository.ProductRepository;
import com.yusufkaraasln.productservice.dto.ProductRequest;
import com.yusufkaraasln.productservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.getName()).
                description(productRequest.getDescription()).
                price(productRequest.getPrice()).
                build();

        productRepository.save(product);
        log.info("Product created: {}", product);

    }


    public List<ProductResponse> getProducts() {

        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> ProductResponse.builder().
                id(product.getId()).
                name(product.getName()).
                description(product.getDescription()).
                price(product.getPrice()).
                build()).toList();


    }

}
