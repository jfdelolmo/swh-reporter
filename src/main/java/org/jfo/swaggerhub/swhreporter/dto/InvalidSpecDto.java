package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidSpecDto {
  
  private String type;
  private String title;
  private String name;
  private Integer numErrors = 0;
  private Set<String> errors = new HashSet<>();
  
}
