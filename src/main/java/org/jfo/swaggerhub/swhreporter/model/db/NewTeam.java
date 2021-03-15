package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class NewTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    private NewCollaboration collaboration;

    @Override
    public String toString() {
        return "NewTeam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
