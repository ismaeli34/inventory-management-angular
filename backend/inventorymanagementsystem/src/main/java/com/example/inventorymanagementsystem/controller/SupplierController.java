package com.example.inventorymanagementsystem.controller;

import com.example.inventorymanagementsystem.dto.CategoryDto;
import com.example.inventorymanagementsystem.dto.SupplierDto;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addSupplier(@RequestBody @Valid SupplierDto supplierDto){
        return ResponseEntity.ok(supplierService.addSupplier(supplierDto));
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllSuppliers(){
        return ResponseEntity.ok(supplierService.getAllSupplier());
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateSupplier(@PathVariable Long id,
                                                   @RequestBody @Valid SupplierDto supplierDto){
        return ResponseEntity.ok(supplierService.updateSupplier(id,supplierDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteSupplier(@PathVariable Long id){
        return ResponseEntity.ok(supplierService.deleteSupplier(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getSupplier(@PathVariable Long id){
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

}
