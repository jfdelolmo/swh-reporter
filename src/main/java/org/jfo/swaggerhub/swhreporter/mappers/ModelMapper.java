package org.jfo.swaggerhub.swhreporter.mappers;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.AdminStatusDto;
import org.jfo.swaggerhub.swhreporter.dto.ApiDto;
import org.jfo.swaggerhub.swhreporter.dto.ClxApiOauth2SecurityDefinitionDto;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.MemberDto;
import org.jfo.swaggerhub.swhreporter.dto.ParticipantDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecInfoDto;
import org.jfo.swaggerhub.swhreporter.dto.TeamDto;
import org.jfo.swaggerhub.swhreporter.model.db.AdminStatus;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewOpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.db.ProjectParticipant;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class ModelMapper {

  public SpecInfoDto specModelToSpecPropertiesDto(NewSpecification model) {
    SpecInfoDto dto = new SpecInfoDto();
    dto.setId(model.getId());
    dto.setSpecName(model.getName());
    dto.setSpecTitle(model.getTitle());
    dto.setSpecDescription(model.getDescription());

    //Properties
    dto.setSpecType(model.getProperties().getType());
    dto.setSpecVersion(model.getProperties().getDefaultVersion());

    return dto;
  }
  
  public CollaborationDto collaborationModelToCollaborationDto(NewCollaboration collaboration) {
    CollaborationDto collaborationDto = new CollaborationDto();
    if (null != collaboration) {
      collaborationDto.setMembers(collaboration
          .getMembers()
          .stream()
          .map(this::collaborationMemberShipToMemberDto)
          .collect(Collectors.toList()));

      collaborationDto.setTeams(collaboration
          .getTeams()
          .stream()
          .map(this::collaborationTeamToTeamDto)
          .collect(Collectors.toList()));
    }
    return collaborationDto;
  }

  private MemberDto collaborationMemberShipToMemberDto(NewMember member) {
    return new MemberDto(member.getName(), member.getRole());
  }

  private TeamDto collaborationTeamToTeamDto(NewTeam team) {
    return new TeamDto(team.getName(), team.getDescription());
  }
  
  public ProjectDto projectModelToDto(Project dbp) {
    ProjectDto dto = new ProjectDto();
    dto.setName(dbp.getName());
    dto.setDescription(dbp.getDescription());
    dbp.getApis().forEach(a -> dto.addApi(a.getName()));
    dbp.getDomains().forEach(d -> dto.addDomain(d.getName()));
    return dto;
  }

  public ParticipantDto projectModelToParticipantDto(ProjectParticipant model) {
    ParticipantDto dto = new ParticipantDto();
    dto.setName(model.getName());
    dto.setRoles(model.getRoles());
    dto.setType(model.getType());
    return dto;
  }

  public ClxApiOauth2SecurityDefinitionDto toSecurityDefinitionsDto(String specType, Set<String> scopes, Set<String> roles, Set<String> audiences) {
    ClxApiOauth2SecurityDefinitionDto securityDefinition = new ClxApiOauth2SecurityDefinitionDto();
    if (TYPE_API.equals(specType)) {
      securityDefinition.setScopes(scopes);
      securityDefinition.setRoles(roles);
      securityDefinition.setAudiences(audiences);
    }
    return securityDefinition;
  }

  public ApiDto buildApiDto(NewSpecification specification, NewOpenApiDocument document, NewCollaboration collaboration,
      Set<String> scopes, Set<String> roles, Set<String> audiences) {
      ApiDto apiDto = new ApiDto();
      apiDto.setCollaboration(new CollaborationDto());

      apiDto.setName(specification.getName());
      apiDto.setType(StringUtils.capitalize(specification.getProperties().getType().toLowerCase()));
      apiDto.setVersion(specification.getProperties().getDefaultVersion());
      apiDto.setCreatedBy(specification.getProperties().getCreatedBy());
      apiDto.setUrl(specification.getProperties().getUrl());
      apiDto.setCreated(specification.getProperties().getCreated());
      apiDto.setUpdated(specification.getProperties().getModified());
      apiDto.setDocument(document.getResolved());

      apiDto.setSecurityDefinitions(
          toSecurityDefinitionsDto(
              specification.getProperties().getType(),
              scopes,
              roles,
              audiences)
      );

      apiDto.setCollaboration(collaborationModelToCollaborationDto(collaboration));

      return apiDto;
  }

  public AdminStatusDto adminStatusToDto(AdminStatus input) {
    AdminStatusDto output = new AdminStatusDto();

    output.setTotalApis(input.getTotalApis());
    output.setTotalDomains(input.getTotalDomains());
    output.setErrorApis(input.getErrorApis());
    output.setErrorDomains(input.getErrorDomains());
    output.setLastUpdate(input.getLastUpdate());        
    
    return output;
  }
}
