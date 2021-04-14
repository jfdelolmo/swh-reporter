package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvalidSpecsReportDto {
  
  private int total;
  private Set<InvalidSpecDto> invalidSpecs = new HashSet<>();
  
}
