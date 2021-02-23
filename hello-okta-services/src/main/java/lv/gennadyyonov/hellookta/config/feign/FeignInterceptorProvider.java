package lv.gennadyyonov.hellookta.config.feign;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.dto.RunAsDetails;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import lv.gennadyyonov.hellookta.services.TokenService;

@RequiredArgsConstructor
public class FeignInterceptorProvider {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    public RequestInterceptor getSsoInterceptor() {
        return new SsoInterceptor(authenticationService);
    }

    public RequestInterceptor getClientCredentialsInterceptor(RunAsDetails runAsDetails) {
        return new ClientCredentialsInterceptor(tokenService, runAsDetails);
    }
}
