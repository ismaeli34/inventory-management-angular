package com.example.inventorymanagementsystem.service;
import com.example.inventorymanagementsystem.dto.ProductDto;
import com.example.inventorymanagementsystem.response.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Response saveProduct(ProductDto productDto, MultipartFile imageFile);

    Response updateProduct(ProductDto productDto,MultipartFile imageFile);

    Response getAllProducts();

    Response  getProductById(Long id);


    Response deleteProduct(Long id);
}
