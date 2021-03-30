package org.jfo.swaggerhub.swhreporter.service.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_PROJECTS;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_SPECS;

@Slf4j
@Component
public class SwhProcessor {

    private final SwhEventPublisher publisher;

    public SwhProcessor(SwhEventPublisher publisher){
        this.publisher = publisher;
    }

    public SwhEventPayload processCallForMyAdmin(){
        return commonPublisher(
                commonPayloadBuilder(SwhEventCommand.CALL_FOR_MY_ADMIN)
        );
    }

    public SwhEventPayload processCallForSpecs(){
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_SPECS)
        );
    }

    public SwhEventPayload processCallForProjects() {
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_PROJECTS)
        );
    }

    private SwhEventPayload commonPublisher(SwhEventPayload payload){
        SwhEvent event = new SwhEvent(this, payload);
        publisher.publish(event);

        return payload;
    }

    private SwhEventPayload commonPayloadBuilder(SwhEventCommand command){
        SwhEventPayload payload = new SwhEventPayload();
        payload.setId(UUID.randomUUID().toString());
        payload.setCommand(command);

        return payload;
    }

}
