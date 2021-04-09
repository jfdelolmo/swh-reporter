package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;

import reactor.core.publisher.Flux;

public interface CustomSpecificationReactiveRepository {
  
  Flux<Specification> getUnresolvedSpecs();
  
}
