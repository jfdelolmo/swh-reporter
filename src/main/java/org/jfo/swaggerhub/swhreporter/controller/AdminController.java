package org.jfo.swaggerhub.swhreporter.controller;

import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.jfo.swaggerhub.swhreporter.service.message.SwhEventPayload;
import org.jfo.swaggerhub.swhreporter.service.message.SwhProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  public static final String ELAPSED_TIME_LOG = "Elapsed time: {}";
  public static final String INDEX_VIEW = "index";

  private final InitializerService initializerService;
  private final SwhProcessor swhProcessor;

  @GetMapping("/load/myadmin")
  public String loadMyAdmin() {
    long startTime = System.currentTimeMillis();
    log.info("Call for MyAdmin processor....");

    SwhEventPayload payload = swhProcessor.processCallForMyAdmin();

    log.info("Payload id: {}", payload.getId());
    return INDEX_VIEW;
  }

  @GetMapping("/load/specs")
  public String loadSpecs() {
    long startTime = System.currentTimeMillis();
    log.info("Call for Specs processor....");

    SwhEventPayload payload = swhProcessor.processCallForSpecs();

    log.info("Payload id: {}", payload.getId());
    return INDEX_VIEW;
  }

  @GetMapping("/load/docs")
  public String loadDocuments() {
    log.info("Call for Documentation processor....");
    long start = System.currentTimeMillis();
    
    swhProcessor.processCallForDocumentation();

    log.info("Message sent to process ({} ms)", System.currentTimeMillis()-start);
    return INDEX_VIEW;
  }
  
  
  @GetMapping("/load/collaborations")
  public String loadCollaboration() {
    log.info("Call for Collaboration processor....");
    long start = System.currentTimeMillis();
    
    swhProcessor.processCallForCollaboration();

    log.info("Message sent to process ({} ms)", System.currentTimeMillis()-start);
    return INDEX_VIEW;
  }
  
  @GetMapping("/load/projects")
  public String loadProjects() {
    log.info("Call for Projects processor....");

    SwhEventPayload payload = swhProcessor.processCallForProjects();
    
    log.info("Payload id: {}", payload.getId());
    return INDEX_VIEW;
  }

  @GetMapping("/load/status")
  public String updateStatus() {
    log.info("Call for Update status processor....");

    SwhEventPayload payload = swhProcessor.processCallUpdateStatus();

    log.info("Payload id: {}", payload.getId());
    return INDEX_VIEW;
  }

}
