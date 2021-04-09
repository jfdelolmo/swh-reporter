package org.jfo.swaggerhub.swhreporter.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SwhWebClient {

    private static final String APIKEY = "a377281d-81e5-4b5b-9391-962439111515";

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
    
    
    public <T> Mono<T> executeCallMono(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz){
        if (null==queryParams){
            queryParams = new LinkedMultiValueMap<>();
        }
        return baseCall(url,uriParams,queryParams)
                .bodyToMono(responseClazz);
    }

    public <T> Flux<T> executeCallFlux(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz){
        if (null==queryParams){
            queryParams = new LinkedMultiValueMap<>();
        }
        return baseCall(url,uriParams,queryParams)
                .bodyToFlux(responseClazz);
    }

    private WebClient.ResponseSpec baseCall(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams){
        if (null==uriParams){
            uriParams = new HashMap<>();
        }

        ExchangeStrategies es = ExchangeStrategies
            .builder()
            .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 2))
            .build();
        
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", APIKEY)
                .defaultUriVariables(uriParams)
                .exchangeStrategies(es)
                .build();

        return webClient
                .get()
                .uri(uri ->uri.queryParams(queryParams).build())
                .retrieve();
    }
    
}
