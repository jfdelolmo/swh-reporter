package org.jfo.swaggerhub.swhreporter.model.db;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Team {

  private String name;
  private String description;

}
