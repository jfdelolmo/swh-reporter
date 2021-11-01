package org.jfo.swaggerhub.swhreporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatusDto {
  
  private Long totalApis;
  private Long totalDomains;
  private Long errorApis;
  private Long errorDomains;
  private LocalDateTime lastUpdate;
//  private Long totalUsers;
//  private Long totalOwners;
//  private Long totalDesigners;
//  private Long totalConsumers;
  
}
