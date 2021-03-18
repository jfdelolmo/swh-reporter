package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class ProjectParticipant {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private String name;
  private String type;
  private String roles;
  
  @ManyToOne
  private Project project;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProjectParticipant)) return false;

    ProjectParticipant that = (ProjectParticipant) o;

    if (!Objects.equals(id, that.id)) return false;
    if (!Objects.equals(name, that.name)) return false;
    if (!Objects.equals(type, that.type)) return false;
    return Objects.equals(roles, that.roles);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (roles != null ? roles.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ProjectParticipant{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", roles='" + roles + '\'' +
        '}';
  }
}
