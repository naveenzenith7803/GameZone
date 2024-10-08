package com.gamezone.Backend.Controller;

import com.gamezone.Backend.Entity.Cart;
import com.gamezone.Backend.Entity.User;
import com.gamezone.Backend.Service.CartService;
import com.gamezone.Backend.Service.UserService;
import com.gamezone.Backend.dto.*;
import com.gamezone.Backend.mapper.CartMapper;
import com.gamezone.Backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;



    // Get all users (DTO)
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(UserMapper::toUserDTO)
                .toList();  // Convert to UserDTO list
    }

    // Get a user by ID (DTO)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(UserMapper.toUserDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a user (DTO)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        User user = UserMapper.toUser(userCreateDTO);
        User createdUser = userService.createUser(user);
        Cart cart=new Cart();
        cart.setUser(createdUser);
        cartService.createCart(cart);
        return ResponseEntity.ok(UserMapper.toUserDTO(createdUser));
    }

    // Update a user (DTO)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDetails) {
        try {
            User user = userService.getUserById(id).orElseThrow(RuntimeException::new);
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(UserMapper.toUserDTO(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        if (user != null) {
            return ResponseEntity.ok(new UserRoleDTO(user.getId(),user.getRole().toString()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}

