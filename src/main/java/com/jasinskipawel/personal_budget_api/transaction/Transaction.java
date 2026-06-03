package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {}

    public Transaction(BigDecimal amount, TransactionType type, String category, String description, LocalDate date, Account account){
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }


    public TransactionType getType() {
        return type;
    }


    public String getCategory() {
        return category;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
