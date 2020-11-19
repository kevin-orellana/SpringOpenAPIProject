package dev.codescreen;

import dev.codescreen.Models.Account;
import dev.codescreen.ServiceInterfaces.UUIDService;
import dev.codescreen.Services.AccountManager;
import dev.codescreen.Utils.Exceptions.AccountAlreadyExist;
import org.junit.Test;

import java.util.UUID;

public class AccountManagerTest {
    @Test
    public void testAccountCreation(){
        String account1UUID = "acc1";
        Account account1 = AccountManager.createAccount(account1UUID);

        AccountManager.persistNewAccount(account1);

        try {
            AccountManager.persistNewAccount(account1);
        } catch (RuntimeException e){
            assert(e instanceof AccountAlreadyExist);
        }
    }
}
