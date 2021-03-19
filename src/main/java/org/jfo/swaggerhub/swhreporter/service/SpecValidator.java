package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecValidator {
  
  public Set<String> wrongReferences(String spec){
    Set<String> errors = new HashSet<>();
    
    IntStream.of(StringUtils.countMatches(spec, "./CommonDefinitions.yaml#/"))
        .forEach(i -> errors.add("./CommonDefinitions.yaml#/"));
    
    return errors;
  }
}
