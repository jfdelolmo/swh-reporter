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
  private Set<SimpleSpecDto> apis = new HashSet<>();
  private Set<SimpleSpecDto> domains = new HashSet<>();
  
  public ProjectDto addApi(SimpleSpecDto api){
    this.apis.add(api);
    return this;
  }

  public ProjectDto addDomain(SimpleSpecDto domain){
    this.domains.add(domain);
    return this;
  }
  
}
