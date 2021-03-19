package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WrongReferenceReportDto {
  
  private Long total;
  private Set<WrongReferenceSpecDto> wrongspecs = new HashSet<>();
  
}
