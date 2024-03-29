package org.jfo.swaggerhub.swhreporter.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Function;

import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwhProcessor {

    private final SwhEventPublisher publisher;
    private final SpecificationRepository specificationReactiveRepository;

    @Deprecated
    public SwhEventPayload processCallForMyAdmin() {
        return commonPublisher(
                commonPayloadBuilder(SwhEventCommand.CALL_FOR_MY_ADMIN)
        );
    }

    public SwhEventPayload processCallForCreateMyAdmin(MyAdminDto myAdminDto) {
        SwhEventPayload createMyAdminPayload = new SwhEventPayload();
        createMyAdminPayload.setCommand(SwhEventCommand.CALL_FOR_MY_ADMIN);
        createMyAdminPayload.getParams().put("myAdmin", myAdminDto);
        return commonPublisher(createMyAdminPayload);
    }

    public SwhEventPayload processCallForSpecs() {
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_SPECS)
        );
    }

    public SwhEventPayload processCallForProjects() {
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_PROJECTS)
        );
    }

    public void processCallForCollaboration() {
        Function<Specification, SwhEventPayload> pubSpec = s -> commonPublisher(loadFromSpec(CALL_FOR_COLLABORATION, s.getId()));
        specificationReactiveRepository
                .findAll()
                .delaySubscription(Duration.of(1, ChronoUnit.SECONDS))
                .subscribe(pubSpec::apply);
    }

    public void processCallForDocumentation() {
        Function<Specification, SwhEventPayload> pubSpec = s -> commonPublisher(loadFromSpec(CALL_FOR_DOCUMENTATION, s.getId()));
        specificationReactiveRepository
                .findAll()
                .delaySubscription(Duration.of(5, ChronoUnit.SECONDS))
                .subscribe(pubSpec::apply);
    }

    public SwhEventPayload processCallUpdateStatus() {
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_UPDATE_STATUS)
        );
    }

    public SwhEventPayload processCallForUsers() {
        return commonPublisher(
                commonPayloadBuilder(CALL_FOR_USERS)
        );
    }

    private SwhEventPayload commonPublisher(SwhEventPayload payload) {
        SwhEvent event = new SwhEvent(this, payload);
        publisher.publish(event);

        return payload;
    }

    private SwhEventPayload commonPayloadBuilder(SwhEventCommand command) {
        SwhEventPayload payload = new SwhEventPayload();
        payload.setId(UUID.randomUUID().toString());
        payload.setCommand(command);

        return payload;
    }

    private SwhEventPayload loadFromSpec(SwhEventCommand command, String uuid) {
        SwhEventPayload payload = commonPayloadBuilder(command);
        payload.getParams().put("specUUID", uuid);
        return payload;
    }

}
