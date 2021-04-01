package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Api {

  private String id;
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Api)) return false;
    Api api = (Api) o;
    return Objects.equal(name, api.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
  
}
