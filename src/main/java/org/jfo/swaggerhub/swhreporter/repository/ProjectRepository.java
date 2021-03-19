package org.jfo.swaggerhub.swhreporter.repository;

import java.util.Set;

import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
  
  Iterable<Project> findAllByOrderByNameAsc();
  Project findByName(String name);
  
  default Project saveOrUpdate(Project project){
    Project existent = findByName(project.getName());
    if (null!=existent){
      project.setId(existent.getId());
    }
    if (!project.equals(existent)){
      save(project);
    }
    return project;
  }
  
}