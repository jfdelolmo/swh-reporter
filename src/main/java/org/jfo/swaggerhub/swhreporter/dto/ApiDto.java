package org.jfo.swaggerhub.swhreporter.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiDto {

  private String type;
  private String name;
  private String version;
  private String project;
  private String url;
  private String createdBy;
  private String document;
  private LocalDateTime created;
  private LocalDateTime updated;
  private CollaborationDto collaboration;
  private ClxApiOauth2SecurityDefinitionDto securityDefinitions;

}
