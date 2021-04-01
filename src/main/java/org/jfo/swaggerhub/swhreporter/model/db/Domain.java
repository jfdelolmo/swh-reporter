package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Domain {

  private String id;
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Domain)) return false;
    Domain domain = (Domain) o;
    return Objects.equal(name, domain.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
  
}
