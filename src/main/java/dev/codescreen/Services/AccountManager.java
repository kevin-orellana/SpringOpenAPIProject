package dev.codescreen.Services;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.AccountBalance;
import dev.codescreen.Models.User;
import dev.codescreen.Utils.AccountStatus;
import dev.codescreen.Utils.DebitOrCredit;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/*
* This implementation of the AccountManagerService uses a local, thread-safe HashTable to
* provide access to Account insttances.
* */
public class AccountManager {
    static Hashtable<UUID, UUID> userUuidToAccountUuid = new Hashtable<UUID, UUID>();
    static Hashtable<UUID, Account> accountUuidToAccount = new Hashtable<UUID, Account>();

    // Current implementation
    static Hashtable<String, String> userIdToAccountId = new Hashtable<String, String>();
    static Hashtable<String, String> accountIdToUserId = new Hashtable<String, String>();
    static Hashtable<String, Account> accountIdToAccount = new Hashtable<String, Account>();

    public static Account findAccountByUserUuid(UUID userUuid) {
        // for now, our AccountManagerService model assumes a 1:1 User to Account cardinality
        UUID accountUuid = userUuidToAccountUuid.get(userUuid);
        return accountUuidToAccount.get(accountUuid);
    }

    public static Account findAccountByUserId(String userId) {
        String accountId = userIdToAccountId.get(userId);
        return accountIdToAccount.get(accountId);
    }

    public static Account createAccount(String accountId){
        Account account = new Account(accountId);
        account.setAccountStatus(AccountStatus.ACTIVE);
        return account;
    }

    /*
    Enforce 1:1 User to Account cardinality. Ideally, a User can have multiple accounts, though.
    Creating an AccountOwner table would be ideal here.
    * */
    public static void linkUserToAccount(User user, Account account){
        // check if Account is already owned by someone
        if (accountIdToUserId.containsKey(user.getUserId())){
            throw new IllegalStateException("Account " + account.getAccountId() + " is already owned by someone else!");
        }
        accountIdToUserId.put(account.getAccountId(), user.getUserId());
        userIdToAccountId.put(user.getUserId(), account.getAccountId());
    }

    public static Account createNewAccountForUser(User user){
        String accountId = user.getUserId() +"-A1";
        Account account = new Account(accountId);

        // TODO: should all accounts have a set currency/debit/balance upon creation?
        AccountBalance accountBalance = new AccountBalance(new BigDecimal(0), "USD", DebitOrCredit.DEBIT);
        persistNewAccount(account);
        return account;
    }

    public static void persistNewAccount(Account account){
        if (!accountIdToAccount.containsKey(account.getAccountId())){
            accountIdToAccount.put(account.getAccountId(), account);
        } else {
            throw new RuntimeException("Account alreaddy persisted");
        }
    }

    public static void batchPersistNewAccount(List<Account> accountList){
        for (Account a: accountList){
            persistNewAccount(a);
        }
    }

    public static Account createAccountIfMissingForUser(User user){
        Account account = findAccountByUserId(user.getUserId());
        if (account == null){
            account = createNewAccountForUser(user);
        }
        return account;
    }
}
