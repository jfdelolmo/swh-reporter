package org.jfo.swaggerhub.swhreporter.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.jfo.swaggerhub.swhreporter.service.StatusService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwhListener {

    private final InitializerService initializerService;
    private final StatusService statusService;

    Map<SwhEventCommand, Function<SwhEventPayload, String>> commandMap = Map.of(
            CALL_FOR_SPECS, this::callForSpecs,
            CALL_FOR_MY_ADMIN, this::callForMyAdmin,
            CALL_FOR_PROJECTS, this::callForProjects,
            CALL_FOR_COLLABORATION, this::callForCollaboration,
            CALL_FOR_DOCUMENTATION, this::callForDocumentation,
            CALL_FOR_UPDATE_STATUS, this::callForUpdateStatus,
            CALL_FOR_USERS, this::callForUsers
    );

    @EventListener
    public void onApplicationEvent(SwhEvent event) {
        log.info("Listening event {}  @ {}", event.getPayload().getId(), event.getTimestamp());
        handleEvent(event.getPayload());
    }

    private void handleEvent(SwhEventPayload payload) {
        commandMap.get(payload.getCommand()).apply(payload);
        log.info("SwhLister consumed :: {} - {}", payload.getCommand(), payload.getId());
    }

    private String callForSpecs(SwhEventPayload payload) {
        long loadedItems = initializerService.initAllOwnedSpecs();
        if (-1L == loadedItems) {
            log.warn("No need to update list of owned specs");
        } else {
            log.info("All owned specs items: {}", loadedItems);
        }
        return payload.getId();
    }

    private String callForMyAdmin(SwhEventPayload payload) {
        String user = initializerService.initMyAdmin((MyAdminDto) payload.getParams().get("myAdmin"));
        if ("".equals(user)) {
            log.error("MyAdmin not initialized");
        } else {
            log.info("MyAdmin for user {}", user);
        }
        return payload.getId();
    }

    private String callForProjects(SwhEventPayload payload) {
        long loadedItems = initializerService.rxInitAllOwnedProjects();
        if (loadedItems > 0) {
            log.info("Projects loaded: {}", loadedItems);
        } else {
            log.warn("No need to update list of owned projects");
        }

        long projectUpdated = initializerService.updateProjectSpecs();
        if (projectUpdated > 0) {
            log.info("Project - updated specs id: {}", projectUpdated);
        }
        return payload.getId();
    }

    private String callForCollaboration(SwhEventPayload payload) {
        String specUuid = (String) payload.getParams().get("specUUID");
        long loaded = initializerService.retrieveCollaborationAndUpdateSpecification(specUuid);
        if (-1L == loaded) {
            log.warn("No collaboration updated for specification {}", specUuid);
        } else {
            log.info("Collaboration updated for specification {}", specUuid);
        }
        return payload.getId();
    }

    private String callForDocumentation(SwhEventPayload payload) {
        String specUuid = (String) payload.getParams().get("specUUID");
        Long loaded = initializerService.retrieveDocumentationAndUpdateSpecification(specUuid);
        if (-1L == loaded) {
            log.warn("No documentation updated for specification {}", specUuid);
        } else {
            log.info("Documentation updated for specification {}", specUuid);
        }
        return payload.getId();
    }

    private String callForUpdateStatus(SwhEventPayload payload) {
        statusService.updateStatus();
        return payload.getId();
    }

    private String callForUsers(SwhEventPayload payload){
      long loadedItems = initializerService.initAllOwnedUsers();
      if (-1L == loadedItems) {
        log.warn("No need to update list of owned users");
      } else {
        log.info("All owned user items: {}", loadedItems);
      }
      return payload.getId();
    }
}
