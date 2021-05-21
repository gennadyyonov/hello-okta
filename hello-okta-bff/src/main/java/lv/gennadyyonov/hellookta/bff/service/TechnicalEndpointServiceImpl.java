package lv.gennadyyonov.hellookta.bff.service;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.Arrays.asList;

@Service
public class TechnicalEndpointServiceImpl implements TechnicalEndpointService {

    private static final Collection<String> ALLOWED_CLASSES = asList(
        "graphql.kickstart.graphiql.boot.GraphiQLController",
        "graphql.kickstart.graphiql.boot.ServletGraphiQLController"
    );
    private static final String[] ALLOWED_ENDPOINTS = {
        // GraphiQL
        "/graphiql", "/vendor/**"
    };

    @Override
    public boolean isAllowed(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = signature.getMethod().getDeclaringClass();
        return ALLOWED_CLASSES.stream().anyMatch(allowedClass -> allowedClass.contains(clazz.getName()));
    }

    @Override
    public String[] getAllowedEndpoints() {
        return ALLOWED_ENDPOINTS;
    }
}
