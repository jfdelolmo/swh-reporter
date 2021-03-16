package org.jfo.swaggerhub.swhreporter.model.swh;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectsJson {

  private Integer offset;
  private Long totalCount;
  private List<Project> projects;

}
