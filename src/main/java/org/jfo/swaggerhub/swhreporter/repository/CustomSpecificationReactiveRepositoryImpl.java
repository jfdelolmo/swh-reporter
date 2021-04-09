package org.jfo.swaggerhub.swhreporter.repository;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.ERROR_RETRIEVE_FROM_SWAGGER_HUB;

import java.util.List;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import reactor.core.publisher.Flux;

public class CustomSpecificationReactiveRepositoryImpl implements CustomSpecificationReactiveRepository {

  private final ReactiveMongoTemplate mongoTemplate;

  @Autowired
  public CustomSpecificationReactiveRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Flux<Specification> getUnresolvedSpecs() {
    Query query = new Query(
        Criteria
            .where("openApiDocument.resolved")
            .is(ERROR_RETRIEVE_FROM_SWAGGER_HUB)
    ); 
    
    return mongoTemplate.find(query, Specification.class);
  }
}
