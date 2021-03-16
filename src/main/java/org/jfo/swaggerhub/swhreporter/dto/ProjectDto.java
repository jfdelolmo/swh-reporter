package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {

  private String name;
  private String description;
  private Set<String> apis = new HashSet<>();
  private Set<String> domains = new HashSet<>();
  
  public ProjectDto addApi(String api){
    this.apis.add(api);
    return this;
  }

  public ProjectDto addDomain(String domain){
    this.domains.add(domain);
    return this;
  }
  
}
