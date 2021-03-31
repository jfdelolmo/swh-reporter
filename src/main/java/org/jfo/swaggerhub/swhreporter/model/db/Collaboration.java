package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Document
public class Collaboration {

    private Set<Team> teams = new HashSet<>();
    private Set<Member> members = new HashSet<>();

    public Collaboration addTeam(Team team) {
        teams.add(team);
        return this;
    }

    public Collaboration addMember(Member member) {
        members.add(member);
        return this;
    }

}
