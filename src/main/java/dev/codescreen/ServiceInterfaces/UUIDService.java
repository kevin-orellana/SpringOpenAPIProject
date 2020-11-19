package dev.codescreen.ServiceInterfaces;

import java.util.Hashtable;
import java.util.UUID;

/*
* A service that provides UUIDs from different UUID pools.
* */
public class UUIDService {

    // emulate grabbing a UUID from a pool of UUIDs by appending the UUID
    private static Hashtable<Class, Integer> classToUUIDPrefixes = new Hashtable<Class, Integer>();
    private static Integer counter = 1;

    public static UUID getUUIDForClass(Class clazz){
        if (classToUUIDPrefixes.containsKey(clazz)){
            return new UUID(classToUUIDPrefixes.get(clazz), 13);
        } else {
            if (counter == Integer.MAX_VALUE){
                // avoid an an integer overflow
                throw new RuntimeException("Out of most significant UUID prefixes - find another UUID pool implementation :)");
            }
            classToUUIDPrefixes.put(clazz, counter++);
            return new UUID(classToUUIDPrefixes.get(clazz), 13);
        }
    }
}
