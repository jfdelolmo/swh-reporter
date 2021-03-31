package org.jfo.swaggerhub.swhreporter.service.message;

import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_COLLABORATION;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_PROJECTS;
import static org.jfo.swaggerhub.swhreporter.service.message.SwhEventCommand.CALL_FOR_SPECS;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.UUID;
import java.util.function.Function;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationReactiveRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwhProcessor {

  private final SwhEventPublisher publisher;
  private final SpecificationReactiveRepository specificationReactiveRepository;

  public SwhEventPayload processCallForMyAdmin() {
    return commonPublisher(
        commonPayloadBuilder(SwhEventCommand.CALL_FOR_MY_ADMIN)
    );
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
