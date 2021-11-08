package org.jfo.swaggerhub.swhreporter.controller;

import java.util.UUID;

import org.jfo.swaggerhub.swhreporter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private static final String REDIRECT_MEMBERS_VIEW = "redirect:/reporter/users";

  private final UserService userService;

  @RequestMapping("/{userUuid}")
  public String deleteUser(Model model, @PathVariable String userUuid) {
    log.info("UserController :: Deleting member {}", userUuid);
    long startTime = System.currentTimeMillis();

    boolean deleted = userService.deleteUser(UUID.fromString(userUuid));
    if (!deleted) {
      log.error("User {} does not exist!", userUuid);
    }

    log.info("UserController :: Deleting member {} :: Elapsed time {} ms", userUuid, System.currentTimeMillis() - startTime);
    return REDIRECT_MEMBERS_VIEW;
  }
}
