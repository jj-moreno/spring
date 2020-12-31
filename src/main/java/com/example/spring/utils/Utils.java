package com.example.spring.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class Utils {

    private final static Random RANDOM = new SecureRandom();
    private final static String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ123456879";
    private final static int PASSWORD_LENGTH = 30;

    public static UUID generateUserUUID() {
        return UUID.randomUUID();
    }

    public static String generateEncryptedPassword() {
        StringBuilder returnValue = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i< PASSWORD_LENGTH; i++){
            if (i % 5 == 0){
                returnValue.append("-");
            }
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return returnValue.toString();
    }
}
