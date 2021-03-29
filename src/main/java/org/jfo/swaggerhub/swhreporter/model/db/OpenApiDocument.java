package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Document
public class OpenApiDocument {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

//    @Lob
    private String resolved;

//    @Lob
    private String unresolved;

    //    @OneToOne
//    private Specification specification;

    public OpenApiDocument(String resolved, String unresolved) {
        this.resolved = resolved;
        this.unresolved = unresolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenApiDocument)) return false;

        OpenApiDocument that = (OpenApiDocument) o;

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
                "resolved='" + (null != resolved ? resolved.substring(0, 15) : "Empty") + '\'' +
                ", unresolved='" + (null != unresolved ? unresolved.substring(0, 15) : "Empty") + '\'' +
                '}';
    }
}
