package dev.codescreen.Utils;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.AccountBalance;
import dev.codescreen.Models.User;
import dev.codescreen.ServiceInterfaces.AccountManagerService;
import dev.codescreen.ServiceInterfaces.ServerHealthService;
import dev.codescreen.ServiceInterfaces.UUIDService;
import dev.codescreen.ServiceInterfaces.UserService;
import dev.codescreen.Services.AccountManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class BootstrapUtil {
    public static final String account1Id = "A001";
    public static final String account2Id = "A002";
    public static final String account3Id = "A003";
    public static final String account4Id = "A004";
    public static final String account5Id = "A005";
    public static final String user1Id    = "U001";
    public static final String user2Id    = "U002";
    public static final String user3Id    = "U003";

    private static boolean serverIsSetup = false;
    /*
    * Create dummy data and persist into system during development
    * */
    public static void bootstrapDev(){
        setupAccountsAndUsers();
    }

    public static void setupAccountsAndUsers(){
        // Set up users and accounts with starting balances
        Account account1 = new Account(account1Id, new AccountBalance(new BigDecimal(99), "USD", DebitOrCredit.DEBIT));
        Account account3 = new Account(account2Id, new AccountBalance(new BigDecimal(50), "EUR", DebitOrCredit.CREDIT));
        Account account2 = new Account(account3Id, new AccountBalance(new BigDecimal(-1), "CNY", DebitOrCredit.DEBIT));
        Account account4 = new Account(account4Id, new AccountBalance(new BigDecimal(10000), "USD", DebitOrCredit.DEBIT));
        Account account5 = new Account(account5Id, new AccountBalance(new BigDecimal(20), "USD", DebitOrCredit.DEBIT));
        AccountManager.batchPersistNewAccount(Arrays.asList(account1, account2, account3, account4, account5));
        User user1 = new User(user1Id);
        User user2 = new User(user2Id);
        User user3 = new User(user3Id);
        // Current project holder values in service.yml
        User user4 = new User("2226e2f9-ih09-46a8-958f-d659880asdfD");
        User user5 = new User("8786e2f9-d472-46a8-958f-d659880e723d");

        UserService.batchPersistNewUser(Arrays.asList(user1, user2, user3, user4, user5));

        AccountManager.linkUserToAccount(user1, account1);
        AccountManager.linkUserToAccount(user2, account2);
        AccountManager.linkUserToAccount(user3, account3);
        AccountManager.linkUserToAccount(user4, account4);
        AccountManager.linkUserToAccount(user5, account5);
    }

    public static void mainSetup(){
        ServerHealthService.setHealthy(true);
        setupAccountsAndUsers();

        // signal the server's setup has finished
        serverIsSetup = true;
    }

    public static boolean isServerSetup(){
        return serverIsSetup;
    }
}
