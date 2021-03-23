package org.jfo.swaggerhub.swhreporter.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;


class RxSwaggerHubServiceImplTest {

  private final SwhWebClient swhWebClient = new SwhWebClient();
  private final RxSwaggerHubService rxService = new RxSwaggerHubServiceImpl(swhWebClient);

  @Test
  @Disabled
  void getAllOwnerSpecs() {
    Flux<ApisJson> flux = rxService.getAllOwnerSpecs("CREALOGIX");
    List<ApisJsonApi> all = new ArrayList<>();
    flux
        .map(apisJson -> all.addAll(apisJson.getApis()))
        .collectList().block();
    Assertions.assertThat(all).isNotEmpty();
    Assertions.assertThat(all.size()).isEqualTo(147);
  }
}