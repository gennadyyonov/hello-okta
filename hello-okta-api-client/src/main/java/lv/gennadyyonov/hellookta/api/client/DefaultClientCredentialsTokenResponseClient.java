package lv.gennadyyonov.hellookta.api.client;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractValueByPattern;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.jsonStringAttributeValueRegex;

public class DefaultClientCredentialsTokenResponseClient extends ClientCredentialsTokenResponseClient<String> {

    private static final Pattern ACCESS_TOKEN_PATTERN = compile(jsonStringAttributeValueRegex("access_token"));

    public DefaultClientCredentialsTokenResponseClient() {
        super(DefaultClientCredentialsTokenResponseClient::extractAccessToken);
    }

    private static String extractAccessToken(String response) {
        return extractValueByPattern(ACCESS_TOKEN_PATTERN, response);
    }
}
