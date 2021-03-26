package org.jfo.swaggerhub.swhreporter.model.db;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class AdminStatus {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private Long totalApis;
  private Long totalDomains;
  private Long errorApis;
  private Long errorDomains;
  private OffsetDateTime lastUpdate;
  
}
