package com.eex.application.controller;

import com.eex.application.dto.AccountRequest;
import com.eex.domain.entity.Account;
import com.eex.domain.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN)")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN)")
    public ResponseEntity<List<Account>> getAccountsByClientId(@PathVariable Integer clientId) {
        List<Account> accounts = accountService.getAccountsByClientId(clientId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN)")
    public ResponseEntity<List<Account>> getAccountsByName(@RequestParam String name) {
        List<Account> accounts = accountService.getAccountsByName(name);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid  Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, @RequestBody @Valid AccountRequest accountDetails) {
        try {
            var accountdomain = new Account();
            accountdomain.setName(accountDetails.getName());
            Account updatedAccount = accountService.updateAccount(id, accountdomain);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

