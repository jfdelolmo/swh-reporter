package org.jfo.swaggerhub.swhreporter.mappers;

import lombok.RequiredArgsConstructor;
import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.model.db.*;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;

@Component
@RequiredArgsConstructor
public class ModelMapper {

    private final OASExtractor oasExtractor;

    public SpecInfoDto specModelToSpecPropertiesDto(Specification model) {
        SpecInfoDto dto = new SpecInfoDto();
        dto.setId(model.getId().toString());
        dto.setSpecName(model.getName());
        dto.setSpecTitle(model.getTitle());
        dto.setSpecDescription(model.getDescription());

        //Properties
        dto.setSpecType(model.getSpecificationProperties().getType());
        dto.setSpecVersion(model.getSpecificationProperties().getDefaultVersion());

        return dto;
    }

    public CollaborationDto collaborationModelToCollaborationDto(Collaboration collaboration) {
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

    private MemberDto collaborationMemberShipToMemberDto(Member member) {
        return new MemberDto(member.getName(), member.getRole());
    }

    private TeamDto collaborationTeamToTeamDto(Team team) {
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

    public ApiDto buildApiDto(Specification specification, OpenApiDocument document, Collaboration collaboration,
                              Set<String> scopes, Set<String> roles, Set<String> audiences) {
        ApiDto apiDto = new ApiDto();
        apiDto.setCollaboration(new CollaborationDto());

        apiDto.setName(specification.getName());
        apiDto.setType(StringUtils.capitalize(specification.getSpecificationProperties().getType().toLowerCase()));
        apiDto.setVersion(specification.getSpecificationProperties().getDefaultVersion());
        apiDto.setCreatedBy(specification.getSpecificationProperties().getCreatedBy());
        apiDto.setUrl(specification.getSpecificationProperties().getUrl());

        apiDto.setCreated(fromDateToLocalDateTime(specification.getSpecificationProperties().getCreated()));
        apiDto.setUpdated(fromDateToLocalDateTime(specification.getSpecificationProperties().getModified()));

        apiDto.setDocument(document.getResolved());

        apiDto.setSecurityDefinitions(
                toSecurityDefinitionsDto(
                        specification.getSpecificationProperties().getType(),
                        scopes,
                        roles,
                        audiences)
        );

        apiDto.setCollaboration(collaborationModelToCollaborationDto(collaboration));

        return apiDto;
    }

    private LocalDateTime fromDateToLocalDateTime(Date d) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneId.systemDefault());
    }

    public AdminStatusDto adminStatusToDto(Status input) {
        AdminStatusDto output = new AdminStatusDto();

        output.setTotalApis(input.getTotalApis());
        output.setTotalDomains(input.getTotalDomains());
        output.setErrorApis(input.getErrorApis());
        output.setErrorDomains(input.getErrorDomains());
        output.setLastUpdate(fromDateToLocalDateTime(input.getLastUpdate()));

        return output;
    }

    public SpecInfoDto specModelToSpecDto(Specification input) {
        SpecInfoDto out = new SpecInfoDto();
        if (null != input) {
            out.setId(input.getId().toString());
            out.setSpecName(input.getName());

            out.setSpecTitle(input.getTitle());
            out.setSpecDescription(input.getDescription());

            //Properties
            if (null != input.getSpecificationProperties()) {
                out.setSpecType(input.getSpecificationProperties().getType());
                out.setSpecVersion(input.getSpecificationProperties().getDefaultVersion());
            }
        }
        return out;
    }

    public ApiDto apiDetailsModelToDto(Specification input) {
        ApiDto out = new ApiDto();

        if (null != input) {
            out.setName(input.getName());
            out.setProject("PENDING TO DEFINE");
            if (null != input.getSpecificationProperties()) {
                out.setType(input.getSpecificationProperties().getType());
                out.setVersion(input.getSpecificationProperties().getDefaultVersion());
                out.setUrl(input.getSpecificationProperties().getUrl());
                out.setCreatedBy(input.getSpecificationProperties().getCreatedBy());
                out.setCreated(fromDateToLocalDateTime(input.getSpecificationProperties().getCreated()));
                out.setUpdated(fromDateToLocalDateTime(input.getSpecificationProperties().getModified()));
            }

            if (null != input.getOpenApiDocument()) {
                out.setDocument(input.getOpenApiDocument().getResolved());
                oasExtractor.init(out.getDocument());
                out.setSecurityDefinitions(
                        toSecurityDefinitionsDto(
                                input.getSpecificationProperties().getType(),
                                oasExtractor.getScopes(),
                                oasExtractor.getRoles(),
                                oasExtractor.getAudiences()));
            }

            if (null!=input.getCollaboration()) {
              out.setCollaboration(collaborationModelToCollaborationDto(input.getCollaboration()));
            }
        }

        return out;
    }

    public ParticipantReportDto projectModelToParticipantReportDto(Project input) {
        ParticipantReportDto output = new ParticipantReportDto();
        if (null!=input) {
            output.setProject(input.getName());
            output.getParticipants().addAll(
                    input.getParticipants()
                            .stream()
                            .map(this::projectModelToParticipantDto)
                            .collect(Collectors.toSet())
            );
        }
        return output;
    }

    public WrongReferenceSpecDto validations(Specification s, Set<String> validate) {
        WrongReferenceSpecDto output = new WrongReferenceSpecDto();
        if (null!=s) {
            output.setName(s.getName());
            output.setTitle(s.getTitle());
            output.setErrors(validate);
            output.setNumErrors(validate.size());
            if (null!=s.getSpecificationProperties()) {
                output.setType(s.getSpecificationProperties().getType());
            }
        }
        return output;
    }
}
