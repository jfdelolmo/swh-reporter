package org.jfo.swaggerhub.swhreporter.model.db;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class OpenApiDocument {

  private String resolved;
  private String unresolved;

  public OpenApiDocument(String resolved, String unresolved) {
    this.resolved = resolved;
    this.unresolved = unresolved;
  }

}
