package org.jfo.swaggerhub.swhreporter.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecPropertiesDto {

    private String specType;
    private String specName;
    private String specVersion;
    private String specDescription;
    private OffsetDateTime created;
    private OffsetDateTime updated;

}
