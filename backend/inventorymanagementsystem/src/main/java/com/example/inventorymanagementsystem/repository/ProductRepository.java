package com.example.inventorymanagementsystem.repository;

import com.example.inventorymanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
