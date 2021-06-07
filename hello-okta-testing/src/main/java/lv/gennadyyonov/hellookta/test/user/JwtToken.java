package lv.gennadyyonov.hellookta.test.user;

import com.fasterxml.jackson.core.type.TypeReference;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lv.gennadyyonov.hellookta.test.JsonUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.asList;
import static java.util.Base64.getUrlDecoder;

@UtilityClass
public class JwtToken {

    private static final int SIGNUM = 1;
    private static final long VALID_FOR_MINUTES = 5L;
    private static final String KEY_PAIR_FILE = "keys/rs256/keypair.json";
    private static final KeyPair KEY_PAIR = JsonUtils.resourceToObject(
        JwtToken.class.getClassLoader().getResourceAsStream(KEY_PAIR_FILE),
        new TypeReference<>() {
        }
    );

    public static Jwt create(String username, List<String> groups) {
        Jwt.Builder builder = Jwt.withTokenValue(createCompact(username, groups));
        headers().forEach(builder::header);
        claims(username, groups).forEach(builder::claim);
        return builder.build();
    }

    /**
     * Build a JWT With a Private Key
     * <br>
     * https://developer.okta.com/docs/guides/build-self-signed-jwt/java/jwt-with-private-key/
     */
    public static String createCompact(String username, List<String> groups) {
        PrivateKey privateKey = privateKey();
        Instant now = Instant.now();
        return Jwts.builder()
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(VALID_FOR_MINUTES, MINUTES)))
            .setSubject(username)
            .setClaims(claims(username, groups))
            .setHeader(headers())
            .setId(UUID.randomUUID().toString())
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact();
    }

    private static Map<String, Object> headers() {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", KEY_PAIR.getAlg());
        header.put("kid", KEY_PAIR.getKid());
        return header;
    }

    private static Map<String, Object> claims(String username, List<String> groups) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimNames.SUB, username);
        claims.put("groups", groups);
        claims.put("scp", asList("email", "openid", "profile"));
        return claims;
    }

    /**
     * Creates Private key (https://developer.okta.com/docs/guides/implement-oauth-for-okta-serviceapp/create-publicprivate-keypair/)
     * <br>
     * Public/Private key pair file ("keys/rs256/keypair.json") file has been created using https://mkjwk.org/
     * <br>
     * https://tools.ietf.org/html/rfc7517 (RSA Private Key Representations and Blinding)
     * https://docs.oracle.com/javase/8/docs/api/java/security/spec/RSAPrivateKeySpec.html 2
     * https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html
     * https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html
     * https://docs.oracle.com/javase/8/docs/api/java/security/KeyFactory.html
     */
    @SneakyThrows
    private static PrivateKey privateKey() {
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(
            new BigInteger(SIGNUM, getUrlDecoder().decode(KEY_PAIR.getN())),
            new BigInteger(SIGNUM, getUrlDecoder().decode(KEY_PAIR.getD()))
        );
        KeyFactory factory = KeyFactory.getInstance(KEY_PAIR.getKty());
        return factory.generatePrivate(rsaPrivateKeySpec);
    }

    @Data
    private static class KeyPair {
        private String kid;
        private String alg;
        private String kty;
        private String d;
        private String n;
    }
}
