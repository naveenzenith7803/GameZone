package com.gamezone.Backend.Repository;

import com.gamezone.Backend.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
