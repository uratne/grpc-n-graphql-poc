package com.kaleris.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaleris.graphql.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
