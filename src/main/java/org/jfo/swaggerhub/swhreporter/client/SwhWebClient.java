package org.jfo.swaggerhub.swhreporter.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SwhWebClient {

  public static final String BASE_URL = "https://api.swaggerhub.com";
  public static final String GET_SPECS_URL = BASE_URL + "/specs";
  public static final String GET_APIS_BY_OWNER = BASE_URL + "/apis/{owner}";
  public static final String API_AS_YAML = "/swagger.yaml";
  public static final String DOMAIN_AS_YAML = "/domain.yaml";
  public static final String GET_API_VERSION_URL = BASE_URL + "/apis/{owner}/{apiName}/{version}" + API_AS_YAML;
  public static final String GET_API_COLLABORATION_URL = BASE_URL + "/apis/{owner}/{api}/.collaboration";
  public static final String GET_DOMAIN_VERSION_URL = BASE_URL + "/domains/{owner}/{domain}/{version}" + DOMAIN_AS_YAML;
  public static final String GET_PROJECTS_BY_OWNER = BASE_URL + "/projects/{owner}";
  public static final String GET_PROJECT_MEMBERS = BASE_URL + "/projects/{owner}/{projectId}/members";

  public static final String USER_MANAGEMENT_MEMBERS_URL = BASE_URL + "/user-management/v1/orgs/{owner}/members";


  private String apiKey;

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return this.apiKey;
  }

  public <T> Mono<T> executeCallMono(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz) {
    if (null == queryParams) {
      queryParams = new LinkedMultiValueMap<>();
    }
    return baseCall(url, uriParams, queryParams, (wc, qParams) -> this.baseCallGet(wc, qParams))
        .bodyToMono(responseClazz);
  }

  public <T> Flux<T> executeCallFlux(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz) {
    if (null == queryParams) {
      queryParams = new LinkedMultiValueMap<>();
    }
    return baseCall(url, uriParams, queryParams, this::baseCallGet)
        .bodyToFlux(responseClazz);
  }

  public <T> Flux<T> executeCallFluxDelete(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz) {
    if (null == queryParams) {
      queryParams = new LinkedMultiValueMap<>();
    }
    return baseCall(url, uriParams, queryParams, this::baseCallDelete)
        .bodyToFlux(responseClazz);
  }

  private WebClient.ResponseSpec baseCall(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, BiFunction<WebClient, MultiValueMap<String, String>, WebClient.ResponseSpec> baseCallerFunction) {
    if (null == uriParams) {
      uriParams = new HashMap<>();
    }

    WebClient webClient = prepareWebClient(url, uriParams);

    return baseCallerFunction.apply(webClient, queryParams);
  }

  private WebClient prepareWebClient(String url, Map<String, ?> uriParams) {
    ExchangeStrategies es = ExchangeStrategies
        .builder()
        .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 3))
        .build();

    return WebClient.builder()
        .baseUrl(url)
        .defaultHeader("Authorization", apiKey)
        .defaultUriVariables(uriParams)
        .exchangeStrategies(es)
        .build();
  }

  private WebClient.ResponseSpec baseCallGet(WebClient webClient, MultiValueMap<String, String> queryParams) {
    return webClient
        .get()
        .uri(uri -> uri.queryParams(queryParams).build())
        .retrieve();
  }

  private WebClient.ResponseSpec baseCallDelete(WebClient webClient, MultiValueMap<String, String> queryParams) {
    return webClient
        .delete()
        .uri(uri -> uri.queryParams(queryParams).build())
        .retrieve();
  }

}
