package org.jfo.swaggerhub.swhreporter.service;

import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RxSwaggerHubService {

    Flux<ApisJson> getAllOwnerSpecs(String owner);

    Mono<Collaboration> getCollaboration(String owner, String url);
    
    Mono<String> getApiVersionByUrl(String url, boolean resolved);

    Flux<ProjectsJson> getProjects(String owner);

    Flux<ProjectMember> getProjectMembers(String owner, String project);

}
