package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Document
public class Collaboration {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    //    @OneToOne
//    private Specification specification;

    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collaboration")
    private Set<Team> teams = new HashSet<>();

    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collaboration")
    private Set<Member> members = new HashSet<>();

    public Collaboration addTeam(Team team) {
        team.setCollaboration(this);
        teams.add(team);
        return this;
    }

    public Collaboration addMember(Member member) {
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

        Collaboration that = (Collaboration) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
