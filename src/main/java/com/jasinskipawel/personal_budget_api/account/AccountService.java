package com.jasinskipawel.personal_budget_api.account;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        if (accountRepository.existsByName(account.getName())) {
            throw new AccountAlreadyExistsException("Konto o podanej nazwie już istnieje!");
        }
        return accountRepository.save(account);
    }
}
