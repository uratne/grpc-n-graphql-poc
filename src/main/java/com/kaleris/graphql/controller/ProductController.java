package com.kaleris.graphql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import com.kaleris.graphql.model.Product;
import com.kaleris.graphql.model.input.ProductInput;
import com.kaleris.graphql.model.input.ProductUpdateInput;
import com.kaleris.graphql.service.ProductService;

import reactor.core.publisher.Flux;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> products() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Product product(@Argument Long id) {
        return productService.getProductById(id)
            .orElse(null);
    }

    @MutationMapping
    public Product createProduct(@Argument("input") ProductInput input) {
        Product product = Product.builder()
            .name(input.getName())
            .description(input.getDescription())
            .price(input.getPrice())
            .build();
        return productService.createProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument("input") ProductUpdateInput input) {
        Product product = Product.builder()
            .name(input.getName())
            .description(input.getDescription())
            .price(input.getPrice())
            .build();
        return productService.updateProduct(input.getId(), product)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @SubscriptionMapping
    public Flux<Product> newProduct() {
        return productService.getNewProductsStream();
    }
}
