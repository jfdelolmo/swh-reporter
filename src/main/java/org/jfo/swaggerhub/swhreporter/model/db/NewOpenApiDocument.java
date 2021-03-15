package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class NewOpenApiDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String definition;

    @OneToOne
    private NewSpecification newSpecification;

    @Override
    public String toString() {
        return "NewOpenApiDocument{" +
                "id=" + id +
                ", definition='" + definition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewOpenApiDocument that = (NewOpenApiDocument) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}
