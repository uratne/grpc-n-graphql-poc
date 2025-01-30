package com.kaleris.graphql.controller;

import com.kaleris.graphql.service.ProductService;
import com.kaleris.graphql.grpc.model.Product;
import com.kaleris.graphql.grpc.model.Empty;
import com.kaleris.graphql.grpc.model.ProductList;
import com.kaleris.graphql.grpc.model.ProductGrpcServiceGrpc.ProductGrpcServiceImplBase;
import com.kaleris.graphql.grpc.model.ProductId;
import com.kaleris.graphql.grpc.model.CreateProductRequest;
import com.kaleris.graphql.grpc.model.UpdateProductRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.util.List;


@GrpcService
public class ProductGrpcController extends ProductGrpcServiceImplBase {
    
    private final ProductService productService;

    public ProductGrpcController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<ProductList> responseObserver) {
        List<com.kaleris.graphql.model.Product> products = productService.getAllProducts();
        ProductList.Builder builder = ProductList.newBuilder();
        products.forEach(p -> builder.addProducts(convertToGrpcProduct(p)));
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProduct(ProductId request, StreamObserver<Product> responseObserver) {
        productService.getProductById(request.getId())
            .ifPresentOrElse(
                p -> {
                    responseObserver.onNext(convertToGrpcProduct(p));
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(
                    new RuntimeException("Product not found with id: " + request.getId())
                )
            );
    }

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<Product> responseObserver) {
        var product = new com.kaleris.graphql.model.Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));
        
        var savedProduct = productService.createProduct(product);
        responseObserver.onNext(convertToGrpcProduct(savedProduct));
        responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<Product> responseObserver) {
        var product = new com.kaleris.graphql.model.Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        if (request.hasPrice()) {
            product.setPrice(BigDecimal.valueOf(request.getPrice()));
        }

        productService.updateProduct(request.getId(), product)
            .ifPresentOrElse(
                p -> {
                    responseObserver.onNext(convertToGrpcProduct(p));
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(
                    new RuntimeException("Product not found with id: " + request.getId())
                )
            );
    }

    @Override
    public void streamNewProducts(Empty request, StreamObserver<Product> responseObserver) {
        productService.getNewProductsStream()
            .map(this::convertToGrpcProduct)
            .subscribe(
                responseObserver::onNext,
                responseObserver::onError,
                responseObserver::onCompleted
            );
    }

    private Product convertToGrpcProduct(com.kaleris.graphql.model.Product product) {
        return Product.newBuilder()
            .setId(product.getId())
            .setName(product.getName())
            .setDescription(product.getDescription())
            .setPrice(product.getPrice().doubleValue())
            .setCreatedAt(product.getCreatedAt().toString())
            .setUpdatedAt(product.getUpdatedAt().toString())
            .build();
    }
}