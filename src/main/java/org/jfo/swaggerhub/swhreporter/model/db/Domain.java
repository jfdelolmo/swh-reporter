package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Domain {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private String name;

  @ManyToOne
  private Project project;

  public Domain(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Domain)) return false;

    Domain domain = (Domain) o;

    if (!Objects.equals(id, domain.id)) return false;
    return Objects.equals(name, domain.name);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Domain{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
  
}
