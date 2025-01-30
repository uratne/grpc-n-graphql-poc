package com.kaleris.graphql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kaleris.graphql.model.User;
import com.kaleris.graphql.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<User> users() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public User user(@Argument Long id) {
        return userService.getUserById(id)
            .orElse(null);
    }

    @MutationMapping
    public User createUser(@Argument("name") String name, @Argument("email") String email) {
        User user = User.builder()
            .name(name)
            .email(email)
            .build();
        return userService.createUser(user);
    }

    @MutationMapping
    public User updateUser(@Argument("id") Long id, @Argument("productId") Long productId) {
        return userService.updateProduct(id, productId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
