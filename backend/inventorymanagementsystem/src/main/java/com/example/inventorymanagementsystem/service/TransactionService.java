package com.example.inventorymanagementsystem.service;

import com.example.inventorymanagementsystem.dto.CategoryDto;
import com.example.inventorymanagementsystem.enums.TransactionStatus;
import com.example.inventorymanagementsystem.payload.TransactionRequest;
import com.example.inventorymanagementsystem.response.Response;

public interface TransactionService {

    Response restockInventory(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response  returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String searchText);

    Response getTransactionById(Long id);

    Response getTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);

}
