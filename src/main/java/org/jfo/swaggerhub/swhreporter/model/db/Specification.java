package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Specification {

  @Id
  private String id = UUID.randomUUID().toString();

  private String name;
  private String title;
  private String description;
  private Boolean hasApi = false;

  private SpecificationProperties specificationProperties;
  private OpenApiDocument openApiDocument;
  private Collaboration collaboration;

}
