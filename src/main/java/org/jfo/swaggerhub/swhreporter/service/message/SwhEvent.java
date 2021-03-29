package org.jfo.swaggerhub.swhreporter.service.message;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SwhEvent extends ApplicationEvent {

    private SwhEventPayload payload;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public SwhEvent(Object source, SwhEventPayload payload) {
        super(source);
        this.payload = payload;
    }


}
