package org.jfo.swaggerhub.swhreporter.exception;

public class MyAdminApiKeyNotFound extends RuntimeException {

  public MyAdminApiKeyNotFound() {
    super("The ApiKey is not defined");
  }

}
