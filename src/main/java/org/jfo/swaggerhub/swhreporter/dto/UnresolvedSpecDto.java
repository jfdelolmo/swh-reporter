package org.jfo.swaggerhub.swhreporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnresolvedSpecDto {

  private String type;
  private String title;
  private String name;
  private String version;
  private String resolved;


}
