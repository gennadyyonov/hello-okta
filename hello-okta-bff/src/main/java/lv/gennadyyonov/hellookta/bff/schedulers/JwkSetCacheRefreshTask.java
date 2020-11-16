package lv.gennadyyonov.hellookta.bff.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.services.JwkSetCacheService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwkSetCacheRefreshTask {

    private static final String REFRESH_TIME = "${schedule.jwk-set-cache-refresh-time}";

    private final JwkSetCacheService jwkSetCacheService;

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Warming-up BFF JWK Set Cache...");
        try {
            jwkSetCacheService.refreshJwkSet();
        } catch (Exception ex) {
            log.error("BFF JWK Set Cache update has failed", ex);
        }
    }

    @Scheduled(initialDelayString = REFRESH_TIME, fixedRateString = REFRESH_TIME)
    public void doRefresh() {
        log.info("Refreshing BFF JWK Set Cache...");
        try {
            jwkSetCacheService.refreshJwkSet();
        } catch (Exception e) {
            log.error("BFF JWK cache refresh error", e);
        }
    }
}
