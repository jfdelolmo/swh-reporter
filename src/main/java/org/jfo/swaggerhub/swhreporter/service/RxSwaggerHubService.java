package org.jfo.swaggerhub.swhreporter.service;

import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RxSwaggerHubService {

    Flux<ApisJson> getAllOwnerSpecs(String owner);

    Mono<Collaboration> getCollaboration(String url);

    Mono<String> extractApiNameFromUrl(String url, String owner);

    Mono<String> getApiVersionByUrl(String url);

}