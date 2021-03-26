package org.jfo.swaggerhub.swhreporter.dto;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatusDto {
  
  private Long totalApis;
  private Long totalDomains;
  private Long errorApis;
  private Long errorDomains;
  private OffsetDateTime lastUpdate;
  
}
