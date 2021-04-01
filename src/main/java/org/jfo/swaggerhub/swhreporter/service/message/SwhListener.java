package org.jfo.swaggerhub.swhreporter.service.message;

import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_COLLABORATION;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_DOCUMENTATION;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_MY_ADMIN;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_PROJECTS;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_SPECS;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_UPDATE_STATUS;

import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.jfo.swaggerhub.swhreporter.service.reactive.RxAdminService;
import org.jfo.swaggerhub.swhreporter.service.reactive.RxStatusService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwhListener {

  private final InitializerService initializerService;
  private final RxStatusService rxStatusService;

  @EventListener
  public void onApplicationEvent(SwhEvent event) {
    log.info("Listening event {}  @ {}", event.getPayload().getId(), event.getTimestamp());
    handleEvent(event.getPayload());
  }

  private void handleEvent(SwhEventPayload payload) {
    if (CALL_FOR_SPECS.equals(payload.getCommand())) {
      log.info("SwhListener::callForSpecs - {}", payload.getId());
      callForSpecs();
    }
    if (CALL_FOR_MY_ADMIN.equals(payload.getCommand())) {
      log.info("SwhListener::callForMyAdmin - {}", payload.getId());
      callForDummyAdmin();
    }
    if (CALL_FOR_PROJECTS.equals(payload.getCommand())) {
      log.info("SwhListener::callForProjects - {}", payload.getId());
      callForProjects();
    }
    if (CALL_FOR_COLLABORATION.equals(payload.getCommand())) {
      log.info("SwhListener::callForCollaboration - {}", payload.getId());
      callForCollaboration(payload);
    }
    if (CALL_FOR_DOCUMENTATION.equals(payload.getCommand())) {
      log.info("SwhListener::callForDocumentation - {}", payload.getId());
      callForDocumentation(payload);
    }
    if (CALL_FOR_UPDATE_STATUS.equals(payload.getCommand())) {
      log.info("SwhListener::callForUpdateStatus - {}", payload.getId());
      callForUpdateStatus(payload);
    }
  }

  private void callForDummyAdmin() {
    String uuid = initializerService.initDummyAdmin();
    if ("".equals(uuid)) {
      log.error("MyAdmin not initialized");
    } else {
      log.info("MyAdmin uuid is {}", uuid);
    }
  }

  private void callForSpecs() {
    long loadedItems = initializerService.rxInitAllOwnedSpecs();
    if (-1L == loadedItems) {
      log.warn("No need to update list of owned specs");
    } else {
      log.info("All owned specs items: {}", loadedItems);
    }
  }

  private void callForProjects() {
    long loadedItems = initializerService.rxInitAllOwnedProjects();
    if (loadedItems>0) {
      log.info("Projects loaded: {}", loadedItems);
    } else {
      log.warn("No need to update list of owned projects");
    }

    long projectUpdated = initializerService.updateProjectSpecs();
    if (projectUpdated>0) {
      log.info("Project - updated specs id: {}", projectUpdated);
    }
  }

  private void callForCollaboration(SwhEventPayload payload) {
    String specUuid = (String) payload.getParams().get("specUUID");
    long loaded = initializerService.retrieveCollaborationAndUpdateSpecification(specUuid);
    if (-1L == loaded) {
      log.warn("No collaboration updated for specification {}", specUuid);
    } else {
      log.info("Collaboration updated for specification {}", specUuid);
    }
  }

  private void callForDocumentation(SwhEventPayload payload) {
    String specUuid = (String) payload.getParams().get("specUUID");
    Long loaded = initializerService.retrieveDocumentationAndUpdateSpecification(specUuid);
    if (-1L == loaded) {
      log.warn("No documentation updated for specification {}", specUuid);
    } else {
      log.info("Documentation updated for specification {}", specUuid);
    }
  }

  private void callForUpdateStatus(SwhEventPayload payload) {
    rxStatusService.updateStatus();
    log.info("Status updated - {}", payload.getId());
  }
}
