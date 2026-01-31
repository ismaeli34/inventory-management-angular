package com.example.inventorymanagementsystem.controller;
import com.example.inventorymanagementsystem.dto.CategoryDto;
import com.example.inventorymanagementsystem.dto.ProductDto;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.CategoryService;
import com.example.inventorymanagementsystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createProduct(
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam("sku") String sku,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam(value = "expiryDate", required = false) String expiryDateStr,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description",required = false) String description){
        ProductDto productDto1 = new ProductDto();
        productDto1.setName(name);
        productDto1.setSku(sku);
        productDto1.setPrice(price);
        productDto1.setStockQuantity(stockQuantity);
        productDto1.setCategoryId(categoryId);
        productDto1.setExpiryDate(LocalDateTime.now().plusYears(1));
        productDto1.setDescription(description);
        log.info(productDto1.toString());
        try{
            return ResponseEntity.ok(productService.saveProduct(productDto1,imageFile));

        } catch (Exception e) {
            log.error("Error saving product: ", e);
            return ResponseEntity.status(500)
                    .body(Response.builder().status(500).message(e.getMessage()).build());
        }
    }




    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }





    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "description",required = false) String description){

        ProductDto productDto1 = new ProductDto();
        productDto1.setName(name);
        productDto1.setSku(sku);
        productDto1.setPrice(price);
        productDto1.setStockQuantity(stockQuantity);
        productDto1.setCategoryId(categoryId);
        productDto1.setProductId(productId);
        productDto1.setDescription(description);

        return ResponseEntity.ok(productService.updateProduct(productDto1,imageFile));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }



}
