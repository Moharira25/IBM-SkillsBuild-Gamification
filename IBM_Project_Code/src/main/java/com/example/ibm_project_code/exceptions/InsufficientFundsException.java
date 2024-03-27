package com.example.ibm_project_code.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InsufficientFundsException extends Exception {
    private BigDecimal accountBalance;
    private BigDecimal requiredAmount;

    public InsufficientFundsException(BigDecimal accountBalance, BigDecimal requiredAmount) {
        super("Insufficient funds for this transaction. Account balance: " + accountBalance +
                ", required amount: " + requiredAmount);
        this.accountBalance = accountBalance;
        this.requiredAmount = requiredAmount;
    }


}

