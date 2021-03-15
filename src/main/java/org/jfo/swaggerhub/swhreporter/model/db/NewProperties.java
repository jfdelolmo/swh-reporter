package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Data
@Entity
public class NewProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;
    private String url;
    private String version;
    private OffsetDateTime created;
    private OffsetDateTime modified;

    @OneToOne
    private NewSpecification newSpecification;

    @Override
    public String toString() {
        return "Properties{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewProperties that = (NewProperties) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(type, that.type)) return false;
        if (!Objects.equals(url, that.url)) return false;
        if (!Objects.equals(version, that.version)) return false;
        if (!Objects.equals(created, that.created)) return false;
        return Objects.equals(modified, that.modified);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }
}
