package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Project {

  @Id
  private String id = UUID.randomUUID().toString();

  private String name;
  private String description;
  private Set<Api> apis = new HashSet<>();
  private Set<Domain> domains = new HashSet<>();
  private Set<ProjectParticipant> participants = new HashSet<>();

  public Project addApi(Api api) {
    this.apis.add(api);
    return this;
  }

  public Project addDomain(Domain domain) {
    this.domains.add(domain);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project)) return false;
    Project project = (Project) o;
    return Objects.equal(name, project.name) && Objects.equal(description, project.description) && Objects.equal(apis, project.apis) && Objects.equal(domains, project.domains) && Objects.equal(participants, project.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, description, apis, domains, participants);
  }
  
}
