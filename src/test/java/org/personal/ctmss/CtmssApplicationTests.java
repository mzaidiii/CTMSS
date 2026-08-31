package org.personal.ctmss;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.personal.ctmss.security.JwtVerificationService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CtmssApplicationTests {

    @Autowired
    private JwtVerificationService jwtVerificationService;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Test
    void contextLoads() {
    }

    @Test
    void generateTokens() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        String adminToken = Jwts.builder()
                .subject("admin@ctmss.org")
                .claims(Map.of("roles", List.of("ROLE_ADMIN", "ROLE_PI", "ROLE_PV")))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)) // 1 year
                .signWith(key)
                .compact();

        System.out.println("=== GENERATED_ADMIN_TOKEN ===");
        System.out.println(adminToken);
        System.out.println("=============================");

        String username = jwtVerificationService.extractUsername(adminToken);
        List<String> roles = jwtVerificationService.extractRoles(adminToken);
        System.out.println("Extracted username: " + username);
        System.out.println("Extracted roles: " + roles);
    }
}
