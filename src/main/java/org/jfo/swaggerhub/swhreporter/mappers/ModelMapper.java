package org.jfo.swaggerhub.swhreporter.mappers;

import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.io.IOUtils;
import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ModelMapper {

    public SpecPropertiesDto specModelToSpecPropertiesDto(NewSpecification model) {
        SpecPropertiesDto dto = new SpecPropertiesDto();
        dto.setId(model.getId());
        dto.setSpecName(model.getName());
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
        ArrayList<SpecPropertiesDto> specPropertiesDtoList = new ArrayList<>();

        specsDto.setNumberOfSpec(apisJson.getTotalCount());

        specPropertiesDtoList.addAll(
                apisJson
                        .getApis()
                        .stream()
                        .map(this::apisJsonApiToBaseSpecDto)
                        .collect(Collectors.toList())
        );
        specsDto.setSpecs(specPropertiesDtoList);

        return specsDto;
    }

    public SpecPropertiesDto apisJsonApiToBaseSpecDto(ApisJsonApi apisJsonApi) {
        SpecPropertiesDto specPropertiesDto = new SpecPropertiesDto();
        specPropertiesDto.setSpecName(apisJsonApi.getName());
        specPropertiesDto.setSpecDescription(apisJsonApi.getDescription().substring(0, 25) + "...");
        specPropertiesDto.setSpecVersion(
                apisJsonApi.getProperties().stream().filter(f -> "X-Version".equals(f.getType())).findFirst().get().getValue()
        );
        apisJsonApi.getProperties().stream()
                .filter(f -> "X-Domain".equals(f.getType()))
                .findFirst()
                .ifPresentOrElse(domain -> specPropertiesDto.setSpecType("DOMAIN"), () -> specPropertiesDto.setSpecType("API"));
        return specPropertiesDto;
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

}
