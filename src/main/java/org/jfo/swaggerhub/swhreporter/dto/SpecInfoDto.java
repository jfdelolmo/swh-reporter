package org.jfo.swaggerhub.swhreporter.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecInfoDto {

    private Long id;
    private String specType;
    private String specTitle;
    private String specName;
    private String specVersion;
    private String specDescription;
    private OffsetDateTime created;
    private OffsetDateTime updated;

}
