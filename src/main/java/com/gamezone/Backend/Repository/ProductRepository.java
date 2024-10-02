package com.gamezone.Backend.Repository;

import com.gamezone.Backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
