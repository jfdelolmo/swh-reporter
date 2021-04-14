package org.jfo.swaggerhub.swhreporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyAdminDto {
  
  private String user;
  private String owner;
  private String apikey;
  private Boolean pendingToUpdate;
  
  
}
