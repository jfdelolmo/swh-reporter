package org.jfo.swaggerhub.swhreporter.mappers;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.API_X_PROPERTY;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_DOMAIN;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.db.Api;
import org.jfo.swaggerhub.swhreporter.model.db.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.db.Domain;
import org.jfo.swaggerhub.swhreporter.model.db.Member;
import org.jfo.swaggerhub.swhreporter.model.db.ProjectParticipant;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.db.SpecificationProperties;
import org.jfo.swaggerhub.swhreporter.model.db.Team;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonProperty;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationTeamMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.springframework.stereotype.Component;

@Component
public class SwhMapper {

  private static final int MAX_DESCRIPTION_LENGTH = 15;
  private static final int SPEC_NAME_POSITION = 3;

  public Specification apisJsonApiToSpecModel(ApisJsonApi apisJsonApi) {
    Specification specification = new Specification();

    SpecificationProperties p = mapToProperties(apisJsonApi.getProperties());
    specification.setSpecificationProperties(p);

    specification.setName(extractNameFromUrl(p.getUrl()));
    specification.setTitle(apisJsonApi.getName());
    int minSize = Math.min(MAX_DESCRIPTION_LENGTH, apisJsonApi.getDescription().length());
    specification.setDescription(StringUtils.isEmpty(apisJsonApi.getDescription()) ?
        "No description provided" :
        apisJsonApi.getDescription().substring(0, minSize) + "..."
    );

    return specification;
  }

  public String extractNameFromUrl(String url) {
    String[] segments = StringUtils.remove(url, SwhWebClient.BASE_URL).split("/");
    return segments[SPEC_NAME_POSITION];
  }

  public SpecificationProperties mapToProperties(List<ApisJsonProperty> input) {
    Map<String, ApisJsonProperty> propertyMap = input.stream().collect(Collectors.toMap(ApisJsonProperty::getType, p -> p));

    SpecificationProperties specificationProperties = new SpecificationProperties();
    specificationProperties.setType(propertyMap.containsKey(API_X_PROPERTY) ? TYPE_API : TYPE_DOMAIN);
    specificationProperties.setUrl(propertyMap.containsKey(API_X_PROPERTY) ? propertyMap.get(API_X_PROPERTY).getUrl() : propertyMap.get("X-Domain").getUrl());
    specificationProperties.setDefaultVersion(propertyMap.get("X-Version").getValue());
    specificationProperties.setStandardization(propertyMap.get("X-Standardization").getValue());
    specificationProperties.setVersions(propertyMap.get("X-Versions").getValue());
    specificationProperties.setCreatedBy(propertyMap.get("X-CreatedBy").getValue());

    specificationProperties.setCreated(propertyToDate(propertyMap.get("X-Created").getValue()));
    specificationProperties.setModified(propertyToDate(propertyMap.get("X-Modified").getValue()));

    return specificationProperties;
  }


  private Date propertyToDate(String propertyValue) {
    OffsetDateTime odt = OffsetDateTime.parse(propertyValue);
    return Date.from(odt.toInstant());
    //document.put(DATE_TIME, Date.from(zonedDateTime.toInstant()));
    //document.put(ZONE, zonedDateTime.getZone().getId());
    //document.put("offset", zonedDateTime.getOffset().toString());
  }


  public Collaboration collaborationSwhToModel(org.jfo.swaggerhub.swhreporter.model.swh.Collaboration input) {
    Collaboration output = new Collaboration();

    if (null != input) {
      input.getMembers().forEach(m -> output.addMember(memberSwhToModel(m)));
      input.getTeams().forEach(t -> output.addTeam(teamSwhToModel(t)));
    }

    return output;
  }

  private Member memberSwhToModel(CollaborationMembership collaborationMembership) {
    Member member = new Member();
    member.setName(collaborationMembership.getName());
    member.setRole(getMainRole(collaborationMembership.getRoles()));
    return member;
  }

  private String getMainRole(List<CollaborationMembership.RolesEnum> roles) {

    if (roles.contains(CollaborationMembership.RolesEnum.EDIT)) {
      return "Designer";
    } else if (roles.contains(CollaborationMembership.RolesEnum.COMMENT)) {
      return "Collaborator";
    } else {
      return "Viewer";
    }
  }

  private Team teamSwhToModel(CollaborationTeamMembership collaborationTeam) {
    Team team = new Team();
    team.setName(collaborationTeam.getName());
    team.setDescription(collaborationTeam.getTitle());
    return team;
  }


  public org.jfo.swaggerhub.swhreporter.model.db.Project projectSwhToModel(Project project) {
    org.jfo.swaggerhub.swhreporter.model.db.Project dbProject = new org.jfo.swaggerhub.swhreporter.model.db.Project();
    dbProject.setName(project.getName());
    dbProject.setDescription(project.getDescription());
    project.getApis().forEach(api -> {
          Api dbApi = new Api();
          dbApi.setName(api);
          dbProject.addApi(dbApi);
        }
    );
    project.getDomains().forEach(api -> {
      Domain domain = new Domain();
      domain.setName(api);
      dbProject.addDomain(domain);
    });
    return dbProject;
  }

  public ProjectParticipant memberShwToParticipants(ProjectMember member) {
    ProjectParticipant participant = new ProjectParticipant();
    
    if (null!=member) {
      participant.setName(member.getName());
      participant.setType(member.getType().getValue());
      participant.setRoles(member
          .getRoles()
          .stream()
          .map(ProjectMember.RolesEnum::getValue)
          .collect(Collectors.joining(", "))
      );
    }

    return participant;
  }
}
