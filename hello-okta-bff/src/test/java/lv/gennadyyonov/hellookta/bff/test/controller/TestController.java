package lv.gennadyyonov.hellookta.bff.test.controller;

import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import lv.gennadyyonov.hellookta.exception.ExternalSystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/access-denied-error")
  public void throwAccessDeniedException() {
    throw new AccessDeniedException("Simulated access denied failure");
  }

  @GetMapping("/external-system-error")
  public void throwExternalSystemException() {
    throw new ExternalSystemException("Simulated external system failure");
  }
}
