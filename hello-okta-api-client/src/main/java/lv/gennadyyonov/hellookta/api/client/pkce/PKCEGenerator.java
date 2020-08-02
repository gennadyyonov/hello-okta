package lv.gennadyyonov.hellookta.api.client.pkce;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Base64.getUrlEncoder;

public class PKCEGenerator {

    private static final int KEY_LENGTH = 96;
    private static final String ALGORITHM = "SHA-256";
    static final String CODE_CHALLENGE = "code_challenge";
    static final String CODE_CHALLENGE_METHOD = "code_challenge_method";
    static final String CODE_VERIFIER = "code_verifier";

    private final StringKeyGenerator keyGenerator = new Base64StringKeyGenerator(getUrlEncoder().withoutPadding(), KEY_LENGTH);

    public Map<String, String> pkceParameters() {
        Map<String, String> attributes = new HashMap<>();
        String codeVerifier = keyGenerator.generateKey();
        attributes.put(CODE_VERIFIER, codeVerifier);
        try {
            String codeChallenge = createHash(codeVerifier);
            attributes.put(CODE_CHALLENGE, codeChallenge);
            attributes.put(CODE_CHALLENGE_METHOD, "S256");
        } catch (NoSuchAlgorithmException e) {
            attributes.put(CODE_CHALLENGE, codeVerifier);
        }
        return attributes;
    }

    private static String createHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        byte[] digest = md.digest(value.getBytes(US_ASCII));
        return getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
