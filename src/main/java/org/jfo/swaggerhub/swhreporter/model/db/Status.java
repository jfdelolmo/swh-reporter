package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Status {

  @Id
  private String id = UUID.randomUUID().toString();

  private Long totalApis;
  private Long totalDomains;
  private Long errorApis;
  private Long errorDomains;
  private Date lastUpdate;

}
