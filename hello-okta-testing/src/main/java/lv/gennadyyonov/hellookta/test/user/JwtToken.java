package lv.gennadyyonov.hellookta.test.user;

import com.fasterxml.jackson.core.type.TypeReference;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lv.gennadyyonov.hellookta.test.JsonTestUtils;
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
  private static final KeyPair KEY_PAIR =
      JsonTestUtils.deserialize(
          JwtToken.class.getClassLoader().getResourceAsStream(KEY_PAIR_FILE),
          new TypeReference<>() {});

  public static Jwt create(String username, List<String> groups) {
    return create(null, username, groups);
  }

  public static Jwt create(String issuer, String username, List<String> groups) {
    Jwt.Builder builder = Jwt.withTokenValue(createCompact(issuer, username, groups));
    headers().forEach(builder::header);
    claims(issuer, username, groups).forEach(builder::claim);
    return builder.build();
  }

  /**
   * <a
   * href="https://developer.okta.com/docs/guides/build-self-signed-jwt/java/jwt-with-private-key/">Build
   * a JWT with a private key</a>
   */
  public static String createCompact(String issuer, String username, List<String> groups) {
    PrivateKey privateKey = privateKey();
    Instant now = Instant.now();
    return Jwts.builder()
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(VALID_FOR_MINUTES, MINUTES)))
        .subject(username)
        .claims(claims(issuer, username, groups))
        .header()
        .add(headers())
        .and()
        .id(UUID.randomUUID().toString())
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
  }

  private static Map<String, Object> headers() {
    Map<String, Object> header = new HashMap<>();
    header.put("alg", KEY_PAIR.getAlg());
    header.put("kid", KEY_PAIR.getKid());
    return header;
  }

  private static Map<String, Object> claims(String issuer, String username, List<String> groups) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(JwtClaimNames.ISS, issuer);
    claims.put(JwtClaimNames.SUB, username);
    claims.put("groups", groups);
    claims.put("scp", asList("email", "openid", "profile"));
    return claims;
  }

  /**
   * <a
   * href="https://developer.okta.com/docs/guides/implement-oauth-for-okta-serviceapp/main/">Implement
   * OAuth for Okta with a service app</a> <br>
   * Public/Private key pair file ("keys/rs256/keypair.json") file has been created using <a
   * href="https://mkjwk.org/">simple JSON Web Key generator</a> <u>References</u>
   *
   * <ul>
   *   <li><a href="https://tools.ietf.org/html/rfc7517">JSON Web Key (JWK)</a> (RSA Private Key
   *       Representations and Blinding)
   *   <li><a
   *       href="https://docs.oracle.com/javase/8/docs/api/java/security/spec/RSAPrivateKeySpec.html">RSAPrivateKeySpec</a>
   *   <li><a
   *       href="https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html">BigInteger</a>
   *   <li><a href="https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html">Base64</a>
   *   <li><a
   *       href="https://docs.oracle.com/javase/8/docs/api/java/security/KeyFactory.html">KeyFactory</a>
   * </ul>
   */
  @SneakyThrows
  private static PrivateKey privateKey() {
    RSAPrivateKeySpec rsaPrivateKeySpec =
        new RSAPrivateKeySpec(
            new BigInteger(SIGNUM, getUrlDecoder().decode(KEY_PAIR.getN())),
            new BigInteger(SIGNUM, getUrlDecoder().decode(KEY_PAIR.getD())));
    KeyFactory factory = KeyFactory.getInstance(KEY_PAIR.getKty());
    return factory.generatePrivate(rsaPrivateKeySpec);
  }

  @SuppressWarnings("MemberName")
  @Data
  private static class KeyPair {
    private String kid;
    private String alg;
    private String kty;
    private String d;
    private String n;
  }
}
