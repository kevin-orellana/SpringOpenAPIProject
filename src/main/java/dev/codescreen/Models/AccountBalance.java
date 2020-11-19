package dev.codescreen.Models;

import dev.codescreen.Utils.DebitOrCredit;

import java.math.BigDecimal;

public class AccountBalance {
    BigDecimal amount;
    String currency;
    DebitOrCredit debitOrCredit;

    public AccountBalance(BigDecimal amount, String currency, DebitOrCredit debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DebitOrCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(DebitOrCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }
}
