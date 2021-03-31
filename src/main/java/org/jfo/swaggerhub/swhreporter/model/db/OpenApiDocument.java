package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class OpenApiDocument {

  @Id
  private String id = UUID.randomUUID().toString();
  private String resolved;
  private String unresolved;

  public OpenApiDocument(String resolved, String unresolved) {
    this.resolved = resolved;
    this.unresolved = unresolved;
  }

}
