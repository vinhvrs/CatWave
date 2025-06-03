package com.catwave.demo.model;

/**
 * Represents a single transaction that the client
 * will POST to /payment/api/transactions/sync.
 */
public class TransactionDto {
    private String id;
    private Double amount;
    private String currency;

    public TransactionDto() {}

    public TransactionDto(String id, Double amount, String currency) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
