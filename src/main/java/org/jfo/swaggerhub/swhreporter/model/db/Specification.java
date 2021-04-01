package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Specification {

  @Id
  private String id = UUID.randomUUID().toString();

  private String name;
  private String title;
  private String description;
  private Boolean hasApi = false;

  private SpecificationProperties specificationProperties;
  private OpenApiDocument openApiDocument;
  private Collaboration collaboration;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Specification)) return false;
    Specification that = (Specification) o;
    return Objects.equal(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "Specification{" +
        "name='" + name + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", hasApi=" + hasApi +
        ", specificationProperties=" + specificationProperties +
        ", openApiDocument=" + openApiDocument +
        ", collaboration=" + collaboration +
        '}';
  }
}
