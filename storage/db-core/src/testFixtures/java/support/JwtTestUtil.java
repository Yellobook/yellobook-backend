<<<<<<< HEAD
package support;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtTestUtil {
    public static String createExpiredToken(Long memberId, SecretKey secretKey) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() - 1))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}

=======
//package support;
//
//import io.jsonwebtoken.Jwts;
//import java.util.Date;
//import javax.crypto.SecretKey;
//
//public class JwtTestUtil {
//    public static String createExpiredToken(Long memberId, SecretKey secretKey) {
//        return Jwts.builder()
//                .claim("memberId", memberId)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() - 1))
//                .signWith(secretKey, Jwts.SIG.HS256)
//                .compact();
//    }
//}
//
>>>>>>> 3e7de0fd2fd05518faf233556db9c9fb42495bf7
