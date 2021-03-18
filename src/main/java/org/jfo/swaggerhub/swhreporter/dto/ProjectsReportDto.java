package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectsReportDto {
  
  private Integer total;
  private Set<ProjectDto> projects = new HashSet<>();
  
  public ProjectsReportDto addProject(ProjectDto projectDto){
    this.projects.add(projectDto);
    return this;
  }
  
}
