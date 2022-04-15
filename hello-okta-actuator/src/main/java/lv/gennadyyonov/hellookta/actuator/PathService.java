package lv.gennadyyonov.hellookta.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoints;

@RequiredArgsConstructor
public class PathService {

    private final PathMappedEndpoints pathMappedEndpoints;

    public String getBasePath() {
        return pathMappedEndpoints.getBasePath();
    }
}
