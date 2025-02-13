package com.yellobook.core.domain.order;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;


public class OrderNumberGenerator {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private OrderNumberGenerator() {
        throw new IllegalStateException("Utility class");
    }

    // 주문번호 생성 메서드
    public static String generateOrderNumber(Long orderId) {
        String randomPart = generateRandomString(6);
        String hashedIdPart = generateHashedId(orderId);

        return "ORD-" + randomPart + "-" + hashedIdPart;
    }

    // 랜덤한 문자열 생성 (A-Z, 0-9 조합)
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARSET.charAt(RANDOM.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }

    // 주문 ID를 해싱하여 4자리 해시 값 생성
    private static String generateHashedId(Long orderId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(orderId.toString()
                    .getBytes(StandardCharsets.UTF_8));

            // 해시 값의 앞 2바이트를 16진수로 변환 (유니크 ID 보장)
            return String.format("%04X", ((hash[0] & 0xFF) << 8) | (hash[1] & 0xFF));
        } catch (Exception e) {
            throw new RuntimeException("Error generating hashed order ID", e);
        }
    }
}

