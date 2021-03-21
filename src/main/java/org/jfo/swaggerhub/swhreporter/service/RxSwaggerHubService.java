package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxSwaggerHubService implements SwaggerHubService {

    private SwhWebClient swhWebClient;

    @Override
    public Flux<ApisJsonApi> getAllOwnerSpecs(String owner) {
        return null;
    }

    @Override
    public Mono<Collaboration> getCollaboration(String url) {
        return null;
    }

    @Override
    public Mono<String> extractApiNameFromUrl(String url, String owner) {
        return null;
    }

    @Override
    public Mono<String> getApiVersionByUrl(String url) {
        return swhWebClient.executeCallMono(url, null, null, String.class);
    }
}
