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
public class Domain {

    @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    private String name;

    //  @ManyToOne
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
