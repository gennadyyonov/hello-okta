package lv.gennadyyonov.hellookta.services;

import org.aspectj.lang.ProceedingJoinPoint;

public interface TechnicalEndpointService {

    boolean isAllowed(ProceedingJoinPoint joinPoint);

    String[] getAllowedEndpoints();
}
