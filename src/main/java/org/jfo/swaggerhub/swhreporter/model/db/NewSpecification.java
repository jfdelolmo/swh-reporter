package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class NewSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private NewProperties properties;

    @OneToOne(cascade = CascadeType.ALL)
    private NewOpenApiDocument openApiDocument;

    @OneToOne(cascade = CascadeType.ALL)
    private NewCollaboration collaboration;

    public NewSpecification setProperties(NewProperties properties){
        properties.setNewSpecification(this);
        this.properties = properties;
        return this;
    }

    public NewSpecification setOpenApiDocument(NewOpenApiDocument openApiDocument){
        openApiDocument.setNewSpecification(this);
        this.openApiDocument = openApiDocument;
        return this;
    }

    public NewSpecification setCollaboration(NewCollaboration newCollaboration){
        newCollaboration.setNewSpecification(this);
        this.collaboration = newCollaboration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewSpecification that = (NewSpecification) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(properties, that.properties)) return false;
        if (!Objects.equals(openApiDocument, that.openApiDocument))
            return false;
        return Objects.equals(collaboration, that.collaboration);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (openApiDocument != null ? openApiDocument.hashCode() : 0);
        result = 31 * result + (collaboration != null ? collaboration.hashCode() : 0);
        return result;
    }
}
