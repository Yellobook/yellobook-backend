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
