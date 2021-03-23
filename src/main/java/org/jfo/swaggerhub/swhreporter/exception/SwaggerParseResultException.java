package org.jfo.swaggerhub.swhreporter.exception;

import java.util.List;
import java.util.Set;

public class SwaggerParseResultException extends RuntimeException {
  
  private List<String> errors;

  public SwaggerParseResultException(String message, List<String> errors) {
    super(message);
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }
}
