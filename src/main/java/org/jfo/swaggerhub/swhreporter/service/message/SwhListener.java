package org.jfo.swaggerhub.swhreporter.service.message;

import java.util.UUID;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwhListener { //implements ApplicationListener<SwhEvent> {

    private final InitializerService initializerService;

//    @Override
    @EventListener
    public void onApplicationEvent(SwhEvent event) {
        log.info("Listening event {}  @ {}", event.getPayload().getId(), event.getTimestamp());
        handleEvent(event.getPayload());
    }

    private void handleEvent(SwhEventPayload payload) {
        if (SwhEventCommand.CALL_FOR_SPECS.equals(payload.getCommand())){
            callForSpecs();
        }
        if (SwhEventCommand.CALL_FOR_MY_ADMIN.equals(payload.getCommand())){
            callForDummyAdmin();
        }
        if (SwhEventCommand.CALL_FOR_PROJECTS.equals(payload.getCommand())){
            callForProjects();
        }
        if (SwhEventCommand.CALL_FOR_COLLABORATION.equals(payload.getCommand())){
            callForCollaboration(payload);
        }
    }

    private void callForDummyAdmin() {
        String uuid = initializerService.initDummyAdmin();
        if ("".equals(uuid)){
            log.error("MyAdmin not initialized");
        }else{
            log.info("MyAdmin uuid is {}", uuid);
        }
    }

    private void callForSpecs() {
        Long loadedItems = initializerService.rxInitAllOwnedSpecs();
        if (-1L == loadedItems){
            log.warn("No need to update list of owned specs");
        }else{
            log.info("All owned specs items: {}", loadedItems);
        }
    }

    private void callForProjects(){
        Long loadedItems = initializerService.rxInitAllOwnedProjects();
        if (-1L == loadedItems){
            log.warn("No need to update list of owned projects");
        }else{
            log.info("All owned project items: {}", loadedItems);
        }
    }
    
    private void callForCollaboration(SwhEventPayload payload){
        String specUuid = (String) payload.getParams().get("specUUID");
        Long loaded = initializerService.retrieveCollaborationAndUpdateSpecification(specUuid);
        if (-1L == loaded){
            log.warn("No collaboration updated for specification {}", specUuid);
        }else{
            log.info("Collaboration updated for specification {}", specUuid);
        }
    }
}
