package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class NewOpenApiDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String resolved;
    
    @Lob
    private String unresolved;

    @OneToOne
    private NewSpecification newSpecification;

    public NewOpenApiDocument(String resolved, String unresolved) {
        this.resolved = resolved;
        this.unresolved = unresolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewOpenApiDocument)) return false;

        NewOpenApiDocument that = (NewOpenApiDocument) o;

        if (!Objects.equals(resolved, that.resolved)) return false;
        return Objects.equals(unresolved, that.unresolved);
    }

    @Override
    public int hashCode() {
        int result = resolved != null ? resolved.hashCode() : 0;
        result = 31 * result + (unresolved != null ? unresolved.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewOpenApiDocument{" +
            "resolved='" + (null!=resolved? resolved.substring(0,15) : "Empty") + '\'' +
            ", unresolved='" + (null!=unresolved? unresolved.substring(0,15) : "Empty") + '\'' +
            '}';
    }
}
