package com.medvedev.backend.controller;

import com.medvedev.backend.dto.AccountSummaryDTO;
import com.medvedev.backend.dto.UserDTO;
import com.medvedev.backend.entity.Account;
import com.medvedev.backend.entity.User;
import com.medvedev.backend.repository.UserRepository;
import com.medvedev.backend.service.AccountService;
import com.medvedev.backend.service.TransactionService;
import com.medvedev.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userRepository.findAll(); //  Fetch users from DB

        List<UserDTO> userDTOs = users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .joinDate(user.getJoinDate())
                        .accounts(user.getAccounts().stream()
                                .map(account -> new AccountSummaryDTO(
                                        account.getId(),
                                        account.getAccountType().name(), //  Convert Enum to String
                                        account.getBalance(),
                                        account.getMaxSpending(), //  Added maxSpending field
                                        account.getTotalTransactions(),
                                        account.getAccountNumber(),
                                        account.getBankName()))
                                .collect(Collectors.toList())) //  Ensure stream is collected properly
                        .build()) //  Properly closes the Builder pattern
                .collect(Collectors.toList()); //  Collect the mapped list

        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Retrieve a user by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Retrieve a user by email")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("Received email lookup request: {}", email);

        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok) // If present, return 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // If empty, return 404 Not Found
    }

    @Operation(summary = "Retrieve a balance by user ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/balance/{userId}")
    public ResponseEntity<UserDTO> getUserBalance(@PathVariable Integer userId) {
        // Fetch the user first
        User user = userService.findUserById(userId);

        List<Account> accounts = accountService.getAccountByUserId(userId);

        List<AccountSummaryDTO> accountDTOs = accounts.stream()
                .map(account -> AccountSummaryDTO.builder()
                        .id(account.getId())
                        .accountType(account.getAccountType().name())
                        .balance(account.getBalance())
                        .maxSpending(BigDecimal.valueOf(5000)) // Example
                        .totalTransactions(
                                transactionService.countTransactionsByAccountId(account.getId())) //  Using new method
                        .build())
                .toList();


        UserDTO userDTO = UserDTO.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .joinDate(user.getJoinDate())
                .accounts(accountDTOs) //  Attach the list of accounts
                .build();

        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Create a new user", description = "Registers a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(summary = "Delete a user", description = "Deletes the user by user ID.")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
