package com.catwave.demo.model;

import java.math.BigDecimal;

public class TransactionDto {
    private String id;
    private BigDecimal amount;
    private String currency;

    public TransactionDto() { }

    public TransactionDto(String id, BigDecimal amount, String currency) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
