package org.jfo.swaggerhub.swhreporter.model.swh;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
  
  private String name;
  private String description;
  private List<String> apis;
  private List<String> domains;
  
}
