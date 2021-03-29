package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document
public class Specification {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private String title;
    private String description;
    private Boolean hasApi = false;

    private SpecificationProperties specificationProperties;
    private OpenApiDocument openApiDocument;
    private Collaboration collaboration;
}
