package lv.gennadyyonov.hellookta.api.client.pkce;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Base64.getUrlEncoder;

public class PkceCodeGenerator {

  private static final int KEY_LENGTH = 96;
  private static final String ALGORITHM = "SHA-256";

  static final String CODE_CHALLENGE = "code_challenge";
  static final String CODE_CHALLENGE_METHOD = "code_challenge_method";
  static final String CODE_VERIFIER = "code_verifier";

  public Map<String, String> pkceParameters() {
    Map<String, String> attributes = new HashMap<>();
    String codeVerifier = generateCodeVerifier();
    attributes.put(CODE_VERIFIER, codeVerifier);
    try {
      String codeChallenge = generateCodeChallenge(codeVerifier);
      attributes.put(CODE_CHALLENGE, codeChallenge);
      attributes.put(CODE_CHALLENGE_METHOD, "S256");
    } catch (NoSuchAlgorithmException e) {
      attributes.put(CODE_CHALLENGE, codeVerifier);
    }
    return attributes;
  }

  private String generateCodeVerifier() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] codeVerifier = new byte[KEY_LENGTH];
    secureRandom.nextBytes(codeVerifier);
    return getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  private String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
    byte[] bytes = codeVerifier.getBytes(US_ASCII);
    MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
    messageDigest.update(bytes, 0, bytes.length);
    byte[] digest = messageDigest.digest();
    return getUrlEncoder().withoutPadding().encodeToString(digest);
  }
}
