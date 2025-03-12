package com.merabill.model;

public class Payment {
    private final String type;
    private final double amount;
    private final String provider;
    private final String transactionRef;

    public Payment(String type, double amount, String provider, String transactionRef) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.transactionRef = transactionRef;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }
}
