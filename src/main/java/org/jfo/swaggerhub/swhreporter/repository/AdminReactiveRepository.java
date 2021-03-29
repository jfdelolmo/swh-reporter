package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.model.db.Status;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AdminReactiveRepository extends ReactiveMongoRepository<Admin, String> {

    Mono<Admin> findByOwner(String owner);

}
