package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private String name;
  private String description;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
  private Set<Api> apis = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
  private Set<Domain> domains = new HashSet<>();
  
  public Project addApi(Api api){
    api.setProject(this);
    this.apis.add(api);
    return this;
  }

  public Project addDomain(Domain domain){
    domain.setProject(this);
    this.domains.add(domain);
    return this;
  }
  
}
