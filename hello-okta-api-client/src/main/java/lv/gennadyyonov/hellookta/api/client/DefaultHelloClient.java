package lv.gennadyyonov.hellookta.api.client;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.extractValueByPattern;
import static lv.gennadyyonov.hellookta.api.client.utils.ResponseUtils.jsonStringAttributeValueRegex;

public class DefaultHelloClient extends HelloClient<String> {

    private static final Pattern TEXT_PATTERN = compile(jsonStringAttributeValueRegex("text"));

    public DefaultHelloClient(ClientConfig config) {
        super(config, DefaultHelloClient::extractCaseUrl);
    }

    @Override
    protected String getAccessToken(ClientCredentialsRequest request) {
        return new DefaultClientCredentialsTokenResponseClient().getTokenResponse(request);
    }

    private static String extractCaseUrl(String response) {
        return extractValueByPattern(TEXT_PATTERN, response);
    }
}
