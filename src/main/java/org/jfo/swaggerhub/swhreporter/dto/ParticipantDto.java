package org.jfo.swaggerhub.swhreporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantDto {

  private String name;
  private String type;
  private String roles;

}
