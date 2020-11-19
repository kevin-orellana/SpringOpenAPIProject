package dev.codescreen.ServiceInterfaces;

import dev.codescreen.Models.Account;
import dev.codescreen.Models.User;

import java.util.UUID;

/*
* Provide a service to manage the maintenance of Account entities.
* $KO: deprecate this.
* */
public interface AccountManagerService {

    public Account findAccountByUserUuid(UUID userUuid);

}
