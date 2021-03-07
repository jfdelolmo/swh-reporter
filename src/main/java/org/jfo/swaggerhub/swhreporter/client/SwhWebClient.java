package org.jfo.swaggerhub.swhreporter.client;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class SwhWebClient {

    private static final String APIKEY = "c9f7e5ad-6eab-4c9a-8a2f-9e0af4316803";
    private static final String BASE_URL = "https://api.swaggerhub.com";

    public static final String GET_SPECS_URL = BASE_URL + "/specs";
    public static final String GET_API_VERSION_URL = BASE_URL + "/apis/{owner}/{apiName}/{version}/swagger.yaml";
    public static final String GET_API_COLLABORATION_URL = BASE_URL + "/apis/{owner}/{api}/.collaboration";

    public <T> Mono<T> executeCall(String url, Map<String, ?> uriParams, MultiValueMap<String, String> queryParams, Class<T> responseClazz){

        if (null==uriParams){
            uriParams = new HashMap<>();
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", APIKEY)
                .defaultUriVariables(uriParams)
                .build();

        return webClient
                .get()
                .uri(uri ->uri.queryParams(queryParams).build())
                .retrieve()
                .bodyToMono(responseClazz);
    }

}
