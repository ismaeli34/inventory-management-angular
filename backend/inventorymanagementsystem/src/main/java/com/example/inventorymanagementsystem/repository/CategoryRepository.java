package com.example.inventorymanagementsystem.repository;

import com.example.inventorymanagementsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
