package dev.codescreen.Models;

/*
* Hold details related to an account's settings. This is optional for an account.
* */
public class AccountConfiguration {
    private boolean canOverdraft;
    private boolean allowsCurrencyConversion;

    public AccountConfiguration(boolean canOverdraft) {
        this.canOverdraft = canOverdraft;
    }

    public boolean allowsOverdrafts(){
        return canOverdraft;
    }

    public boolean allowsCurrencyConversion(){
        return allowsCurrencyConversion;
    }
}
