package com.example.inventorymanagementsystem.controller;
import com.example.inventorymanagementsystem.enums.TransactionStatus;
import com.example.inventorymanagementsystem.payload.TransactionRequest;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> restockInventory(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.restockInventory(transactionRequest));
    }
    @PostMapping("/sell")
    public ResponseEntity<Response> sell(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String searchText


            ){
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, searchText));
    }


    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getAllTransactionsByMonthAndYear(
            @RequestParam int month,
            @RequestParam  int year
    ){
        return ResponseEntity.ok(transactionService.getTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<Response> updateTransactionStatus(@PathVariable Long transactionId,
                                                   @RequestBody @Valid TransactionStatus status){
        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId,status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }



}
