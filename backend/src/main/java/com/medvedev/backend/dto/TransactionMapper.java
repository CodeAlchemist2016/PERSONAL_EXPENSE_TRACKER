package com.medvedev.backend.dto;

import com.medvedev.backend.entity.Transaction;

public interface TransactionMapper {
    TransactionResponseDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionResponseDTO request);
}
