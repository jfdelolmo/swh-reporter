package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Admin {

  @Id
  private String id = UUID.randomUUID().toString();

  private String owner;
  private String apikey;
  private Boolean pendingToUpdate;

}
