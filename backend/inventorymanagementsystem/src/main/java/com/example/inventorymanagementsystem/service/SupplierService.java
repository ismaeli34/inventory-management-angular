package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.dto.SupplierDto;
import com.example.inventorymanagementsystem.response.Response;

public interface SupplierService {

    Response addSupplier(SupplierDto supplierDto);

    Response getAllSupplier();

    Response  getSupplierById(Long id);

    Response updateSupplier(Long id, SupplierDto supplierDto);

    Response deleteSupplier(Long id);
}
