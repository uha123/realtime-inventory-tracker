package com.inventory.tracker.service;

import com.inventory.tracker.dto.TransactionRequestDto;
import com.inventory.tracker.dto.TransactionResponseDto;
import com.inventory.tracker.model.Inventory;
import com.inventory.tracker.model.InventoryTransaction;
import com.inventory.tracker.model.User;
import com.inventory.tracker.repository.InventoryRepository;
import com.inventory.tracker.repository.InventoryTransactionRepository;
import com.inventory.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public List<TransactionResponseDto> getAllTransactions() {
        log.info("Fetching all inventory transactions");
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto getTransactionById(Long id) {
        log.info("Fetching transaction by id: {}", id);
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDto(transaction);
    }

    public List<TransactionResponseDto> getTransactionsByInventoryId(Long inventoryId) {
        log.info("Fetching transactions for inventory id: {}", inventoryId);
        return transactionRepository.findByInventoryId(inventoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto request) {
        log.info("Creating new transaction for inventory id: {}", request.getInventoryId());
        
        Inventory inventory = inventoryRepository.getReferenceById(request.getInventoryId());
        User user = userRepository.getReferenceById(request.getCreatedBy());

        InventoryTransaction transaction = InventoryTransaction.builder()
                .inventory(inventory)
                .transactionType(InventoryTransaction.TransactionType.valueOf(request.getTransactionType()))
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .createdBy(user)
                .build();
        return mapToDto(transactionRepository.save(transaction));
    }

    private TransactionResponseDto mapToDto(InventoryTransaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .inventoryId(transaction.getInventory() != null ? transaction.getInventory().getId() : null)
                .transactionType(transaction.getTransactionType().name())
                .quantity(transaction.getQuantity())
                .reason(transaction.getReason())
                .createdBy(transaction.getCreatedBy() != null ? transaction.getCreatedBy().getId() : null)
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
