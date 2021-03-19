package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WrongReferenceSpecDto {
  
  private String type;
  private String name;
  private Long numErrors;
  private Set<String> errors = new HashSet<>();
  
  public WrongReferenceSpecDto addError(String error){
    this.errors.add(error);
    return this;
  }
  
}
