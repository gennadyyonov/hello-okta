package lv.gennadyyonov.hellookta.api.service;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.Arrays.asList;

@Service
public class TechnicalEndpointServiceImpl implements TechnicalEndpointService {

    private static final Collection<Class<?>> ALLOWED_CLASSES = asList(
            // Springdoc OpenAPI 3.0
            SwaggerWelcomeCommon.class,
            OpenApiResource.class
    );
    private static final String[] ALLOWED_ENDPOINTS = {
            // Springdoc OpenAPI 3.0
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    @Override
    public boolean isAllowed(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = signature.getMethod().getDeclaringClass();
        return ALLOWED_CLASSES.stream().anyMatch(allowedClass -> allowedClass.isAssignableFrom(clazz));
    }

    @Override
    public String[] getAllowedEndpoints() {
        return ALLOWED_ENDPOINTS;
    }
}
