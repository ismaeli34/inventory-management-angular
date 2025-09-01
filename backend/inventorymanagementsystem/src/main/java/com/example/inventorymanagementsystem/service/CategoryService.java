package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.dto.CategoryDto;
import com.example.inventorymanagementsystem.response.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);

    Response getAllCategories();

    Response  getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDto categoryDto);

    Response deleteCategory(Long id);

}
