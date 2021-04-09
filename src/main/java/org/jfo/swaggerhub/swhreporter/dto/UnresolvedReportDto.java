package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnresolvedReportDto {

  private Long total;
  private Set<UnresolvedSpecDto> unresolvedSpecs = new HashSet<>();

}
