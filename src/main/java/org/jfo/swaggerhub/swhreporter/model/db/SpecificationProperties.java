package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Document
public class SpecificationProperties {

    @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    private String type;
    private String url;
    private String defaultVersion;
    private String createdBy;
    private String versions;
    private String standardization;
    private Date created;
    private Date modified;

    //  @OneToOne
//    private Specification specification;

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
        if (!(o instanceof SpecificationProperties)) return false;
        SpecificationProperties that = (SpecificationProperties) o;
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
