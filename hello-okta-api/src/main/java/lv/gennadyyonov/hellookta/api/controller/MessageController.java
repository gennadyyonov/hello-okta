package lv.gennadyyonov.hellookta.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lv.gennadyyonov.hellookta.api.service.MessageService;
import lv.gennadyyonov.hellookta.aspects.HasRole;
import lv.gennadyyonov.hellookta.common.dto.Message;
import lv.gennadyyonov.hellookta.logging.ParameterLogging;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static lv.gennadyyonov.hellookta.api.constants.HelloOktaApiSecurityConsts.MESSAGE_READ;
import static lv.gennadyyonov.hellookta.constants.SecurityConstants.ALLOWED_USERS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@HasRole(
    alias = ALLOWED_USERS,
    roles = {MESSAGE_READ})
@RestController
@RequiredArgsConstructor
public class MessageController implements ParameterLogging {

  private final MessageService messageService;

  @Operation(summary = "Say Hello", description = "Returns Greeting for logged in user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "4xx", description = "Bad Request", content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content)
      })
  @PostMapping(value = "/hello", produces = APPLICATION_JSON_VALUE)
  public Message hello() {
    return messageService.sayHello();
  }
}
