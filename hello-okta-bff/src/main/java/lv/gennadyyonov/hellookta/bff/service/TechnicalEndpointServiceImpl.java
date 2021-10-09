package lv.gennadyyonov.hellookta.bff.service;

import lv.gennadyyonov.hellookta.services.TechnicalEndpointService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

@Service
public class TechnicalEndpointServiceImpl implements TechnicalEndpointService {

    @Override
    public boolean isAllowed(ProceedingJoinPoint joinPoint) {
        return false;
    }

    @Override
    public boolean environmentSupported() {
        return true;
    }

    @Override
    public String[] getAllowedEndpoints() {
        return new String[0];
    }
}
