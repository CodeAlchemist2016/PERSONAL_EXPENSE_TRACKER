package com.medvedev.backend.service;

import com.medvedev.backend.dto.AccountSummaryDTO;
import com.medvedev.backend.entity.Account;
import com.medvedev.backend.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    // Returns List<Account> instead of Optional
    public List<Account> getAccountByUserId(Integer userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.isEmpty() ? Collections.emptyList() : accounts;
    }

    // Returns List<AccountSummaryDTO> for structured API responses
    public List<AccountSummaryDTO> getAccountSummariesByUserId(Integer userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.isEmpty()
                ? Collections.emptyList()
                : accounts.stream()
                .map(account -> modelMapper.map(account, AccountSummaryDTO.class))
                .toList();
    }

    // Calculates total balance across all user accounts
    public BigDecimal getUserBalance(Integer userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Integer id, Account updatedAccount) {
        return accountRepository.findById(id)
                .map(existingAccount -> {
                    existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
                    existingAccount.setBalance(updatedAccount.getBalance());
                    existingAccount.setBankName(updatedAccount.getBankName());
                    existingAccount.setAccountType(updatedAccount.getAccountType());
                    return accountRepository.save(existingAccount);
                })
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }


    public void deleteAccount(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Account not found for ID: " + id);
        }
        accountRepository.deleteById(id);
    }
}
