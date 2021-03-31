package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class ProjectParticipant {

  @Id
  private String id = UUID.randomUUID().toString();

  private String name;
  private String type;
  private String roles;

}
