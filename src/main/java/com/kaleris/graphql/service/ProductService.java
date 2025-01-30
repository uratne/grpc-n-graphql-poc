package com.kaleris.graphql.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kaleris.graphql.model.Product;
import com.kaleris.graphql.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final Sinks.Many<Product> productSink = Sinks.many().multicast().onBackpressureBuffer();

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        productSink.tryEmitNext(savedProduct);
        return savedProduct;
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        log.info("Updating product with id: {}", id);
        return productRepository.findById(id)
            .map(existingProduct -> {
                if (productDetails.getName() != null) {
                    existingProduct.setName(productDetails.getName());
                }
                if (productDetails.getDescription() != null) {
                    existingProduct.setDescription(productDetails.getDescription());
                }
                if (productDetails.getPrice() != null) {
                    existingProduct.setPrice(productDetails.getPrice());
                }
                return productRepository.save(existingProduct);
            });
    }

    public Flux<Product> getNewProductsStream() {
        return productSink.asFlux();
    }
}
