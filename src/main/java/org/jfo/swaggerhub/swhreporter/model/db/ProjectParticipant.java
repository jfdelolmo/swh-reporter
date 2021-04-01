package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class ProjectParticipant {

  @Id
  private String id = UUID.randomUUID().toString();

  private String name;
  private String type;
  private String roles;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProjectParticipant)) return false;
    ProjectParticipant that = (ProjectParticipant) o;
    return Objects.equal(name, that.name) && Objects.equal(type, that.type) && Objects.equal(roles, that.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, type, roles);
  }
  
}
