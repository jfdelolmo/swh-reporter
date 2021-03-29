package org.jfo.swaggerhub.swhreporter.mappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.jfo.swaggerhub.swhreporter.exception.OpenAPIParseResultException;
import org.jfo.swaggerhub.swhreporter.exception.OpenApiSecurityExtractorException;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OASExtractor {

  private OAuthFlows clxApiOAuth2Flows;
  private Map<String, Object> extensions;

  public OASExtractor init(String apiAsString) {
    OpenAPI api = parseOpenApi(apiAsString);
    clxApiOAuth2Flows = Optional.ofNullable(api.getComponents())
        .map(Components::getSecuritySchemes)
        .map(ss -> ss.get("ClxApiOAuth2"))
        .map(SecurityScheme::getFlows)
        .orElseThrow(() -> new OpenApiSecurityExtractorException("ClxApiOAuth2 flow not defined"));

    extensions = Optional.ofNullable(clxApiOAuth2Flows.getAuthorizationCode())
        .map(OAuthFlow::getExtensions)
        .orElse(Collections.emptyMap());
    return this;
  }

  private OpenAPI parseOpenApi(String apiAsString) throws OpenAPIParseResultException {
    OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();
    ParseOptions options = new ParseOptions();
    options.setResolveFully(true);
    SwaggerParseResult parseResults = openAPIV3Parser.readContents(apiAsString, null, options);
    if (!parseResults.getMessages().isEmpty()) {
      throw new OpenAPIParseResultException("Exception on parsing api", parseResults.getMessages());
    }
    return parseResults.getOpenAPI();
  }

  public Set<String> getScopes() {
    return Optional.ofNullable(clxApiOAuth2Flows.getAuthorizationCode())
        .map(OAuthFlow::getScopes)
        .map(LinkedHashMap::keySet).orElse(Collections.emptySet());
  }

  public Set<String> getRoles() {
    if (null != extensions.get("x-clx-roles")) {
      return ((Map<?, ?>) extensions.get("x-clx-roles")).keySet().stream().map(Object::toString).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  public Set<String> getAudiences() {
    if (null != extensions.get("x-clx-audiences")) {
      return ((Map<?, ?>) extensions.get("x-clx-audiences")).keySet().stream().map(Object::toString).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

}
