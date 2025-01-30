package com.kaleris.graphql.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaleris.graphql.model.User;
import com.kaleris.graphql.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateProduct(Long id, Long productId) {
        return productService.getProductById(productId).map(product -> {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            user.setProductId(product.getId());
            return userRepository.save(user);
        });
    }
}
