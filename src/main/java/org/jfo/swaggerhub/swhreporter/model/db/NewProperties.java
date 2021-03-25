package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class NewProperties {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String type;
  private String url;
  private String defaultVersion;
  private String createdBy;
  private String versions;
  private String standardization;
  private OffsetDateTime created;
  private OffsetDateTime modified;

  @OneToOne
  private NewSpecification newSpecification;

  @Override
  public String toString() {
    return "NewProperties{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", url='" + url + '\'' +
        ", defaultVersion='" + defaultVersion + '\'' +
        ", createdBy='" + createdBy + '\'' +
        ", versions='" + versions + '\'' +
        ", standardization='" + standardization + '\'' +
        ", created=" + created +
        ", modified=" + modified +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NewProperties)) return false;
    NewProperties that = (NewProperties) o;
    return Objects.equal(id, that.id) &&
        Objects.equal(type, that.type) &&
        Objects.equal(url, that.url) &&
        Objects.equal(defaultVersion, that.defaultVersion) &&
        Objects.equal(createdBy, that.createdBy) &&
        Objects.equal(versions, that.versions) &&
        Objects.equal(standardization, that.standardization) &&
        Objects.equal(created, that.created) &&
        Objects.equal(modified, that.modified);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, type, url, defaultVersion, createdBy, versions, standardization, created, modified);
  }
}
