package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectReactiveRepository extends ReactiveMongoRepository<Project, String> {

  Flux<Project> findAll();
  Mono<Project> findByName(String name);

  Flux<Project> findAllByOrderByNameAsc();

//    Iterable<Project> findAllByOrderByNameAsc();
//
//    Project findByName(String name);
//
//    default Project saveOrUpdate(Project project) {
//        Project existent = findByName(project.getName());
//        if (null != existent) {
//            project.setId(existent.getId());
//        }
//        if (!project.equals(existent)) {
//            save(project);
//        }
//        return project;
//    }


}
