package org.jfo.swaggerhub.swhreporter.model.db;

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

  public Project addParticipant(ProjectParticipant participant) {
    this.participants.add(participant);
    return this;
  }

}
