package org.jfo.swaggerhub.swhreporter.mappers;

import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.swh.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelMapper {

    public SpecPropertiesDto specModelToSpecPropertiesDto(Specification model){
        SpecPropertiesDto dto = new SpecPropertiesDto();
        dto.setSpecName(model.getName());
        dto.setSpecDescription(model.getDescription());

        //Properties
        dto.setSpecType(model.getProperties().getType());
        dto.setSpecVersion(model.getProperties().getVersion());
        dto.setCreated(model.getProperties().getCreated());
        dto.setUpdated(model.getProperties().getModified());
        
        return dto;    
    }
    
    public SpecsDto apisJsonToSpecDto(ApisJson apisJson){
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
        specPropertiesDto.setSpecDescription(apisJsonApi.getDescription().substring(0,25) + "...");
        specPropertiesDto.setSpecVersion(
                apisJsonApi.getProperties().stream().filter(f->"X-Version".equals(f.getType())).findFirst().get().getValue()
        );
        apisJsonApi.getProperties().stream()
                .filter(f->"X-Domain".equals(f.getType()))
                .findFirst()
                .ifPresentOrElse( domain -> specPropertiesDto.setSpecType("DOMAIN"), () -> specPropertiesDto.setSpecType("API") );
        return specPropertiesDto;
    }

    public CollaborationDto collaborationToCollaborationDto(Collaboration collaboration) {
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

    private MemberDto collaborationMemberShipToMemberDto(CollaborationMembership collaborationMembership) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberName(collaborationMembership.getName());
        memberDto.setMemberRole(getMainRole(collaborationMembership.getRoles()));
        return memberDto;
    }

    private String getMainRole(List<CollaborationMembership.RolesEnum> roles){

        if(roles.contains(CollaborationMembership.RolesEnum.EDIT)) {
            return "Designer";
        }else if (roles.contains(CollaborationMembership.RolesEnum.COMMENT)) {
            return "Collaborator";
        }else {
            return "Viewer";
        }
    }

    private TeamDto collaborationTeamToTeamDto(CollaborationTeamMembership collaborationTeam){
        return new TeamDto(collaborationTeam.getName(), collaborationTeam.getTitle());
    }
}
