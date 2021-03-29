package org.jfo.swaggerhub.swhreporter.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.model.CommonConcepts;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpecValidator {

  public static final String WRONG_COMMON_DEFINITIONS_YAML = "./CommonDefinitions.yaml#/";
  public static final String WRONG_OAUTH_FLOW = "ClxApiOAuth2 authorization code flows is not used";

  public Set<String> validate(String unresolvedAPI, String type){
    Set<String> errors = new HashSet<>(wrongReferences(unresolvedAPI));

    if (CommonConcepts.TYPE_API.equalsIgnoreCase(type)) {
      errors.addAll(wrongOauthFlow(unresolvedAPI));
    }

    return errors;
  }


  public Set<String> wrongReferences(String spec) {
    Set<String> errors = new HashSet<>();
    int matches = StringUtils.countMatches(spec, WRONG_COMMON_DEFINITIONS_YAML);
    IntStream.range(0, matches)
        .forEach(i -> errors.add(WRONG_COMMON_DEFINITIONS_YAML));

    return errors;
  }
  
  public Set<String> wrongOauthFlow(String spec){
    Set<String> errors = new HashSet<>();
    Map<String, Object> map = new Yaml().load(spec);
    boolean error = Optional.ofNullable(map)
        .map(l-> l.get("components"))
        .map(l -> ((LinkedHashMap) l).get("securitySchemes"))
        .map(l -> ((LinkedHashMap) l).get("ClxApiOAuth2"))
        .map(l -> ((LinkedHashMap) l).get("flows"))
        .map(l -> ((LinkedHashMap) l).get("authorizationCode")).isEmpty();
    if (error){
      errors.add(WRONG_OAUTH_FLOW);
    }
    return errors;
  }
}
