package lv.gennadyyonov.hellookta.connectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class OktaConnector {

    private static final String USER_INFO_PATH = "/v1/userinfo";

    private final String userInfoUri;

    public OktaConnector(String issuerUrl) {
        this.userInfoUri = issuerUrl + USER_INFO_PATH;
    }

    public Map<String, Object> getUserInfo(Map<String, String> headers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::set);
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
        //noinspection unchecked
        return response.getBody();
    }
}
