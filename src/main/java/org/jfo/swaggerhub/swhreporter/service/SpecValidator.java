package org.jfo.swaggerhub.swhreporter.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecValidator {

  public static final String WRONG_COMMON_DEFINITIONS_YAML = "./CommonDefinitions.yaml#/";
  public static final String WRONG_OAUTH_FLOW = "ClxApiOAuth2 authorization code flows is not used";

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
