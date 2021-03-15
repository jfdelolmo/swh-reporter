package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class NewCollaboration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private NewSpecification newSpecification;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collaboration")
    private Set<NewTeam> teams = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collaboration")
    private Set<NewMember> members = new HashSet<>();

    public NewCollaboration addTeam(NewTeam team) {
        team.setCollaboration(this);
        teams.add(team);
        return this;
    }

    public NewCollaboration addMember(NewMember member) {
        member.setCollaboration(this);
        members.add(member);
        return this;
    }

    @Override
    public String toString() {
        return "NewCollaboration{" +
                "id=" + id +
                ", members=" + members +
                ", teams=" + teams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewCollaboration that = (NewCollaboration) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
