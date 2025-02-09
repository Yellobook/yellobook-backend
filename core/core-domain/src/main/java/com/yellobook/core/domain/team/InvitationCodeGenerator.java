package com.yellobook.core.domain.team;

import java.security.SecureRandom;

public class InvitationCodeGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    public static String generateNanoId() {
        int length = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder nanoId = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            nanoId.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return nanoId.toString();
    }
}
