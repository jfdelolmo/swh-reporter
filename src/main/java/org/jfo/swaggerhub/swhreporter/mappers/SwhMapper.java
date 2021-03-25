package org.jfo.swaggerhub.swhreporter.mappers;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.API_X_PROPERTY;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_DOMAIN;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.exception.OpenAPIParseResultException;
import org.jfo.swaggerhub.swhreporter.model.db.Api;
import org.jfo.swaggerhub.swhreporter.model.db.Domain;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewProperties;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.db.ProjectParticipant;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonProperty;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationTeamMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@Component
public class SwhMapper {
  
  private static final int MAX_DESCRIPTION_LENGTH = 15;
  private static final int SPEC_NAME_POSITION = 3;

  public NewSpecification apisJsonApiToSpecModel(ApisJsonApi apisJsonApi) {
    NewSpecification specification = new NewSpecification();

    NewProperties p = mapToProperties(apisJsonApi.getProperties());
    specification.setProperties(p);

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

  public NewProperties mapToProperties(List<ApisJsonProperty> input) {
    Map<String, ApisJsonProperty> propertyMap = input.stream().collect(Collectors.toMap(ApisJsonProperty::getType, p -> p ));

    NewProperties properties = new NewProperties();
    properties.setType(propertyMap.containsKey(API_X_PROPERTY)? TYPE_API: TYPE_DOMAIN);
    properties.setUrl(propertyMap.containsKey(API_X_PROPERTY)? propertyMap.get(API_X_PROPERTY).getUrl() : propertyMap.get("X-Domain").getUrl());
    properties.setDefaultVersion(propertyMap.get("X-Version").getValue());
    properties.setStandardization(propertyMap.get("X-Standardization").getValue());
    properties.setVersions(propertyMap.get("X-Versions").getValue());
    properties.setCreatedBy(propertyMap.get("X-CreatedBy").getValue());
    properties.setCreated(OffsetDateTime.parse(propertyMap.get("X-Created").getValue()));
    properties.setModified(OffsetDateTime.parse(propertyMap.get("X-Modified").getValue()));
    
    return properties;
  }

  public NewCollaboration collaborationSwhToModel(Collaboration input) {
    NewCollaboration output = new NewCollaboration();

    input.getMembers().forEach(m -> output.addMember(memberSwhToModel(m)));
    input.getTeams().forEach(t -> output.addTeam(teamSwhToModel(t)));

    return output;
  }

  private NewMember memberSwhToModel(CollaborationMembership collaborationMembership) {
    NewMember member = new NewMember();
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

  private NewTeam teamSwhToModel(CollaborationTeamMembership collaborationTeam) {
    NewTeam team = new NewTeam();
    team.setName(collaborationTeam.getName());
    team.setDescription(collaborationTeam.getTitle());
    return team;
  }



  public org.jfo.swaggerhub.swhreporter.model.db.Project projectSwhToModel(Project project) {
    org.jfo.swaggerhub.swhreporter.model.db.Project dbProject = new org.jfo.swaggerhub.swhreporter.model.db.Project();
    dbProject.setName(project.getName());
    dbProject.setDescription(project.getDescription());
    project.getApis().forEach(api -> dbProject.addApi(new Api(api)));
    project.getDomains().forEach(api -> dbProject.addDomain(new Domain(api)));
    return dbProject;
  }
  
  public ProjectParticipant memberShwToParticipants(ProjectMember member){
    ProjectParticipant participant = new ProjectParticipant();
    participant.setName(member.getName());
    participant.setType(member.getType().getValue());
    participant.setRoles(member
        .getRoles()
        .stream()
        .map(ProjectMember.RolesEnum::getValue)
        .collect(Collectors.joining(", "))
    );
    
    return participant;
  }
}
