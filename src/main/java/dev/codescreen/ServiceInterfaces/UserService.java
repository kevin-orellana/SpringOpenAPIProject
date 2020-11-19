package dev.codescreen.ServiceInterfaces;

import dev.codescreen.Models.User;
import dev.codescreen.Utils.BootstrapUtil;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/*
 * Provide a service to manage the maintenance of User entities.
 * */
public class UserService {
    private static Hashtable<String, User> idToUser = new Hashtable<String, User>();
    public static User createNewUser(String userId){
        return new User(userId);
    }

    public static void persistNewUser(User user){
        if (idToUser.containsKey(user.getUserId())){
            throw new RuntimeException("User already exists");
        }
        idToUser.put(user.getUserId(), user);
    }
    public static User getUserById(String userId){
        if (idToUser.containsKey(userId)){
            return idToUser.get(userId);
        } else{
            throw new RuntimeException("User with id " + userId + " does not exist!");
        }
    }

    public static void batchPersistNewUser(List<User> userList){
        for (User user: userList){
            persistNewUser(user);
        }
    }
}
