package org.jfo.swaggerhub.swhreporter.service.message;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SwhEventPayload {
    private String id;
    private SwhEventCommand command;
    private Map<String, Object> params = new HashMap<>();
}
