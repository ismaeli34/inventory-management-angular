package com.example.inventorymanagementsystem.service.impl;

import com.example.inventorymanagementsystem.dto.TransactionDto;
import com.example.inventorymanagementsystem.entity.Product;
import com.example.inventorymanagementsystem.entity.Supplier;
import com.example.inventorymanagementsystem.entity.Transaction;
import com.example.inventorymanagementsystem.entity.User;
import com.example.inventorymanagementsystem.enums.TransactionStatus;
import com.example.inventorymanagementsystem.enums.TransactionType;
import com.example.inventorymanagementsystem.exception.NameValueRequiredException;
import com.example.inventorymanagementsystem.exception.NotFoundException;
import com.example.inventorymanagementsystem.payload.TransactionRequest;
import com.example.inventorymanagementsystem.repository.ProductRepository;
import com.example.inventorymanagementsystem.repository.SupplierRepository;
import com.example.inventorymanagementsystem.repository.TransactionRepository;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.TransactionService;
import com.example.inventorymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ProductRepository productRepository;



    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and resave
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Made sucessfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();


        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));


        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and resave
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Sold sucessfully")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        // update the stock quantity and resave
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction returned sucessfully initialized")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText,pageRequest);

        List<TransactionDto> transactionDtos = modelMapper
                .map(transactionPage.getContent(),new TypeToken<List<TransactionDto>>() {}.getType());
        transactionDtos.forEach(transactionDtoItem -> {
            transactionDtoItem.setUser(null);
                    transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Transaction returned sucessfully initialized")
                .transactions(transactionDtos)
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository
                .findById(id).orElseThrow(()-> new NotFoundException("Transaction Not Found"));

        TransactionDto transactionDto = modelMapper.map(transaction,TransactionDto.class);
        transactionDto.getUser().setTransactions(null); // removing user transaction list

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDto)
                .build();

    }

    @Override
    public Response getTransactionByMonthAndYear(int month, int year) {

        List<Transaction> transactions = transactionRepository.findAllByMonthAndYear(month,year);

        List<TransactionDto> transactionDtos = modelMapper
                .map(transactions,new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDtos.forEach(transactionDtoItem -> {
            transactionDtoItem.setUser(null);
            transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDtos)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepository
                .findById(transactionId).orElseThrow(()-> new NotFoundException("Transaction Not Found"));
        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Sucessfully Updated")
                .build();
    }
}
