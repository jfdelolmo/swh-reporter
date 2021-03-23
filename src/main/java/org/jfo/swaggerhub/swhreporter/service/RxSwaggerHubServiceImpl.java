package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.LIMIT_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.ORDER_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.OWNER_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.PAGE_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.SORT_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.buildAllOwnerSpecsParams;

import java.util.concurrent.atomic.AtomicInteger;

import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxSwaggerHubServiceImpl implements RxSwaggerHubService {

  private final SwhWebClient swhWebClient;

  private Flux<ApisJson> getAllOwnerSpecsPaged(String owner, int page) {
    
    MultiValueMap<String, String> queryParams = buildAllOwnerSpecsParams(owner, page);

    return swhWebClient.executeCallFlux(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);
  }



  @Override
  public Flux<ApisJson> getAllOwnerSpecs(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    return getAllOwnerSpecsPaged(owner, 0)
        .expand(r -> {
          if (r.getTotalCount() > r.getOffset() + r.getApis().size()) {
            return getAllOwnerSpecsPaged(owner, pageCounter.incrementAndGet());
          }
          return Flux.empty();
        });
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
//        return swhWebClient.executeCallMono(url, null, null, String.class);
    return null;
  }
}
