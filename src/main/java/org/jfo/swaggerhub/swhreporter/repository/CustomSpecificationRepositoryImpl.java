package org.jfo.swaggerhub.swhreporter.repository;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.ERROR_RETRIEVE_FROM_SWAGGER_HUB;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import reactor.core.publisher.Flux;

public class CustomSpecificationRepositoryImpl implements CustomSpecificationRepository {

  private final ReactiveMongoTemplate mongoTemplate;

  @Autowired
  public CustomSpecificationRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
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
