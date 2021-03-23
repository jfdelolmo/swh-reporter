package org.jfo.swaggerhub.swhreporter.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.MemberDto;
import org.jfo.swaggerhub.swhreporter.dto.ParticipantDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecInfoDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.dto.TeamDto;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.db.ProjectParticipant;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;

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
        dto.setSpecVersion(model.getProperties().getVersion());
        dto.setCreated(model.getProperties().getCreated());
        dto.setUpdated(model.getProperties().getModified());

        return dto;
    }

    public SpecsDto apisJsonToSpecDto(ApisJson apisJson) {
        SpecsDto specsDto = new SpecsDto();
        ArrayList<SpecInfoDto> specInfoDtoList = new ArrayList<>();

        specsDto.setNumberOfSpec(apisJson.getTotalCount());

        specInfoDtoList.addAll(
                apisJson
                        .getApis()
                        .stream()
                        .map(this::apisJsonApiToBaseSpecDto)
                        .collect(Collectors.toList())
        );
        specsDto.setSpecs(specInfoDtoList);

        return specsDto;
    }

    public SpecInfoDto apisJsonApiToBaseSpecDto(ApisJsonApi apisJsonApi) {
        SpecInfoDto specInfoDto = new SpecInfoDto();
        specInfoDto.setSpecName(apisJsonApi.getName());
        specInfoDto.setSpecDescription(apisJsonApi.getDescription().substring(0, 25) + "...");
        specInfoDto.setSpecVersion(
                apisJsonApi.getProperties().stream().filter(f -> "X-Version".equals(f.getType())).findFirst().get().getValue()
        );
        apisJsonApi.getProperties().stream()
                .filter(f -> "X-Domain".equals(f.getType()))
                .findFirst()
                .ifPresentOrElse(domain -> specInfoDto.setSpecType("DOMAIN"), () -> specInfoDto.setSpecType("API"));
        return specInfoDto;
    }

    public CollaborationDto collaborationModelToCollaborationDto(NewCollaboration collaboration) {
        CollaborationDto collaborationDto = new CollaborationDto();

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

        return collaborationDto;
    }

    private MemberDto collaborationMemberShipToMemberDto(NewMember member) {
        return new MemberDto(member.getName(), member.getRole());
    }

    private TeamDto collaborationTeamToTeamDto(NewTeam team) {
        return new TeamDto(team.getName(), team.getDescription());
    }


    public String getClobAsString(Clob clobObject) {
        try {
            InputStream in = clobObject.getAsciiStream();
            StringWriter w = new StringWriter();
            IOUtils.copy(in, w);
            String clobAsString = w.toString();
            return clobAsString;
        } catch (Exception e) {
            return "";
        }
    }

    public OpenAPI getOpenApiObjectFromBlob(Blob blob) throws SQLException, IOException, ClassNotFoundException {
        OpenAPI openAPI = new OpenAPI();
        InputStream binaryStream = blob.getBinaryStream();
        try (ObjectInputStream ois = new ObjectInputStream(binaryStream)) {
            openAPI = (OpenAPI) ois.readObject();
        }
        return openAPI;
    }

  public ProjectDto projectModelToDto(Project dbp) {
      ProjectDto dto = new ProjectDto();
      dto.setName(dbp.getName());
      dto.setDescription(dbp.getDescription());
      dbp.getApis().forEach(a->dto.addApi(a.getName()));
      dbp.getDomains().forEach(d->dto.addDomain(d.getName()));
      return dto;
  }

  public ParticipantDto projectModelToParticipantDto(ProjectParticipant model) {
      ParticipantDto dto = new ParticipantDto();
      dto.setName(model.getName());
      dto.setRoles(model.getRoles());
      dto.setType(model.getType());
      return dto;
  }
}
