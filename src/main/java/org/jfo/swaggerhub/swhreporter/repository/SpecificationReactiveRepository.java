package org.jfo.swaggerhub.swhreporter.repository;

import java.util.ArrayList;
import java.util.List;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpecificationReactiveRepository extends ReactiveMongoRepository<Specification, String> {

  Mono<Specification> findByName(String name);

  default Flux<Specification> saveOrUpdateAll(Iterable<Specification> specifications) {
    List<Specification> newSpecs = new ArrayList<>();
    specifications.forEach(s -> {
      Specification existent = findByName(s.getName()).block();
      if (null != existent) {
        if (!existent.equals(s)) {
          existent.setDescription(s.getDescription());
          existent.setTitle(s.getTitle());
          existent.getSpecificationProperties().setModified(s.getSpecificationProperties().getModified());
          existent.getSpecificationProperties().setStandardization(s.getSpecificationProperties().getStandardization());
          existent.getSpecificationProperties().setType(s.getSpecificationProperties().getType());
          existent.getSpecificationProperties().setUrl(s.getSpecificationProperties().getUrl());
          existent.getSpecificationProperties().setDefaultVersion(s.getSpecificationProperties().getDefaultVersion());
          existent.getSpecificationProperties().setVersions(s.getSpecificationProperties().getVersions());
          newSpecs.add(save(existent).block());
        }
      } else {
        newSpecs.add(save(s).block());
      }
    });
    
    return Flux.fromIterable(newSpecs);
  }

}
