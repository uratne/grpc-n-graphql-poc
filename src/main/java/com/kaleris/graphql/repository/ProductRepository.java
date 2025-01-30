package com.kaleris.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kaleris.graphql.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
