package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document
public class Member {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    private String name;
    private String role;

    //    @ManyToOne
    private Collaboration collaboration;

    @Override
    public String toString() {
        return "NewTeam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + role + '\'' +
                '}';
    }
}
