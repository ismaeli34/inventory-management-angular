package com.example.inventorymanagementsystem.service.impl;

import com.example.inventorymanagementsystem.dto.CategoryDto;
import com.example.inventorymanagementsystem.dto.SupplierDto;
import com.example.inventorymanagementsystem.dto.UserDto;
import com.example.inventorymanagementsystem.entity.Category;
import com.example.inventorymanagementsystem.entity.Supplier;
import com.example.inventorymanagementsystem.exception.NotFoundException;
import com.example.inventorymanagementsystem.repository.CategoryRepository;
import com.example.inventorymanagementsystem.repository.SupplierRepository;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDto supplierDto) {
        Supplier supplier = modelMapper.map(supplierDto, Supplier.class);
        supplierRepository.save(supplier);
        return Response.builder().status(200).message("Supplier created successfully").build();
    }
    @Override
    public Response getAllSupplier() {
        List<Supplier> supplierList = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<SupplierDto> supplierDtos = modelMapper.map(supplierList, new TypeToken<List<UserDto>>() {}.getType());
        return Response.builder().status(200).message("success").suppliers(supplierDtos).build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Supplier Not Found"));
        SupplierDto supplerDto = modelMapper.map(supplier, SupplierDto.class);
        return Response.builder().status(200).message("success").supplier(supplerDto).build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Supplier Not Found"));
        if(supplierDto.getName()!=null) existingSupplier.setName(supplierDto.getName());
        if(supplierDto.getAddress()!=null) existingSupplier.setAddress(supplierDto.getAddress());

        supplierRepository.save(existingSupplier);
        return Response.builder().status(200).message("Category Successfully Updated").supplier(supplierDto).build();
    }

    @Override
    public Response deleteSupplier(Long id) {
     supplierRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Supplier Not Found"));
        supplierRepository.deleteById(id);
        return Response.builder().status(200).message("Supplier " + id+ " deleted Successfully").build();
    }

}
