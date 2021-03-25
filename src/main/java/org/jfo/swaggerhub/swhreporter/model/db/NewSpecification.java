package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class NewSpecification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private String title;
  private String description;
  private Boolean hasApi = false;

  @OneToOne(cascade = CascadeType.ALL)
  private NewProperties properties;

  @OneToOne(cascade = CascadeType.ALL)
  private NewOpenApiDocument openApiDocument;

  @OneToOne(cascade = CascadeType.ALL)
  private NewCollaboration collaboration;

  public boolean hasApi() {
    return this.hasApi;
  }
  
  public boolean hasCollaboration() {
    return null != this.getCollaboration();
  }

  public NewSpecification setProperties(NewProperties properties) {
    properties.setNewSpecification(this);
    this.properties = properties;
    return this;
  }

  public NewSpecification setOpenApiDocument(NewOpenApiDocument openApiDocument) {
    this.hasApi = true;
    openApiDocument.setNewSpecification(this);
    this.openApiDocument = openApiDocument;
    return this;
  }

  public NewSpecification setCollaboration(NewCollaboration newCollaboration) {
    newCollaboration.setNewSpecification(this);
    this.collaboration = newCollaboration;
    return this;
  }

  public NewSpecification updateCollaboration(NewCollaboration newCollaboration) {
    if (null != newCollaboration) {
      if (null == this.getCollaboration()) {
        this.setCollaboration(new NewCollaboration());
      }
      this.getCollaboration().setMembers(newCollaboration.getMembers());
      this.getCollaboration().setTeams(newCollaboration.getTeams());
    }
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewSpecification that = (NewSpecification) o;

    if (!Objects.equals(name, that.name)) return false;
    if (!Objects.equals(title, that.title)) return false;
    if (!Objects.equals(description, that.description)) return false;
    if (!Objects.equals(hasApi, that.hasApi)) return false;
    if (!Objects.equals(properties, that.properties)) return false;
    if (!Objects.equals(openApiDocument, that.openApiDocument)) return false;
    return Objects.equals(collaboration, that.collaboration);
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (hasApi != null ? hasApi.hashCode() : 0);
    result = 31 * result + (properties != null ? properties.hashCode() : 0);
    result = 31 * result + (openApiDocument != null ? openApiDocument.hashCode() : 0);
    result = 31 * result + (collaboration != null ? collaboration.hashCode() : 0);
    return result;
  }
}
