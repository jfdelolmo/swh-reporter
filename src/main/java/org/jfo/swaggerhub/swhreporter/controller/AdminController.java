package org.jfo.swaggerhub.swhreporter.controller;

import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.service.AdminService;
import org.jfo.swaggerhub.swhreporter.service.message.SwhEventPayload;
import org.jfo.swaggerhub.swhreporter.service.message.SwhProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private static final String ELAPSED_TIME_LOG = "AdminController :: Elapsed time for payload {} :: {} ms";
  private static final String REDIRECT_INDEX_VIEW = "redirect:/index";

  private final SwhProcessor swhProcessor;
  private final AdminService adminService;

  @Deprecated
  @GetMapping("/load/myadmin")
  public String loadMyAdmin(Model model) {
    log.info("AdminController :: Load MyAdmin....");
    long startTime = System.currentTimeMillis();

    model.addAttribute("myAdmin", adminService.getMyAdmin());

    log.info("AdminController :: Loaded MyAdmin in {} ms", System.currentTimeMillis() - startTime);
    return "admin/myadmin";
  }

  @PostMapping("load/myadmin")
  public String createMyAdmin(MyAdminDto myAdmin, Model model) {
    log.info("AdminController :: MyAdmin processor....");
    long startTime = System.currentTimeMillis();

    SwhEventPayload payload = swhProcessor.processCallForCreateMyAdmin(myAdmin);

    log.info(ELAPSED_TIME_LOG, payload.getId(), System.currentTimeMillis() - startTime);
    return REDIRECT_INDEX_VIEW;
  }

  @GetMapping("/load/specs")
  public String loadSpecs() {
    log.info("AdminController :: Specs processor....");
    long startTime = System.currentTimeMillis();

    SwhEventPayload payload = swhProcessor.processCallForSpecs();

    log.info(ELAPSED_TIME_LOG, payload.getId(), System.currentTimeMillis() - startTime);
    return REDIRECT_INDEX_VIEW;
  }

  @GetMapping("/load/docs")
  public String loadDocuments() {
    log.info("AdminController :: Documentation processor....");
    long start = System.currentTimeMillis();

    swhProcessor.processCallForDocumentation();

    log.info("AdminController :: Documentation processor elapsed time :: {})", System.currentTimeMillis() - start);
    return REDIRECT_INDEX_VIEW;
  }


  @GetMapping("/load/collaborations")
  public String loadCollaboration() {
    log.info("AdminController :: Collaboration processor....");
    long start = System.currentTimeMillis();

    swhProcessor.processCallForCollaboration();

    log.info("AdminController :: Collaboration processor elapsed time :: {})", System.currentTimeMillis() - start);
    return REDIRECT_INDEX_VIEW;
  }

  @GetMapping("/load/projects")
  public String loadProjects() {
    log.info("AdminController :: Projects processor....");
    long start = System.currentTimeMillis();

    SwhEventPayload payload = swhProcessor.processCallForProjects();

    log.info(ELAPSED_TIME_LOG, payload.getId(), System.currentTimeMillis() - start);
    return REDIRECT_INDEX_VIEW;
  }

  @GetMapping("/load/status")
  public String updateStatus() {
    log.info("AdminController :: Update status processor....");
    long start = System.currentTimeMillis();

    SwhEventPayload payload = swhProcessor.processCallUpdateStatus();

    log.info(ELAPSED_TIME_LOG, payload.getId(), System.currentTimeMillis() - start);
    return REDIRECT_INDEX_VIEW;
  }

}
