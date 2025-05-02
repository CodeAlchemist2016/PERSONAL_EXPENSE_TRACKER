package com.medvedev.backend.service;

import com.medvedev.backend.entity.AccountsHistory;
import com.medvedev.backend.repository.AccountsHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountHistoryService {

    private final AccountsHistoryRepository accountsHistoryRepository;

//    @Autowired
//    public AccountHistoryService(AccountsHistoryRepository accountsHistoryRepository) {
//        this.accountsHistoryRepository = accountsHistoryRepository;
//    }

    public List<AccountsHistory> getHistoryByAccount(Integer accountId) {
        List<AccountsHistory> historyRecords = accountsHistoryRepository.findByAccountId(accountId);
        return historyRecords.isEmpty() ? Collections.emptyList() : historyRecords; // Returns empty list if no records exist
    }

    public AccountsHistory addAccountHistory(AccountsHistory accountsHistory) {
        return accountsHistoryRepository.save(accountsHistory);
    }

    public void deleteAccountHistory(Integer id) {
        if (accountsHistoryRepository.existsById(id)) {
            accountsHistoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Account history not found for ID: " + id);
        }
    }
}

