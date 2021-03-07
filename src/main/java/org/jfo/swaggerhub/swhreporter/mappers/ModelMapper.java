package org.jfo.swaggerhub.swhreporter.mappers;

import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.model.swh.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelMapper {

    public SpecDto apisJsonToSpecDto(ApisJson apisJson){
        SpecDto specDto = new SpecDto();
        ArrayList<BaseSpecDto> baseSpecDtoList = new ArrayList<>();

        specDto.setNumberOfSpec(apisJson.getTotalCount());
        specDto.setOffset(apisJson.getOffset());
        baseSpecDtoList.addAll(
                apisJson
                        .getApis()
                        .stream()
                        .map(this::apisJsonApiToBaseSpecDto)
                        .collect(Collectors.toList())
        );
        specDto.setSpecs(baseSpecDtoList);

        return specDto;
    }

    public BaseSpecDto apisJsonApiToBaseSpecDto(ApisJsonApi apisJsonApi) {
        BaseSpecDto baseSpecDto = new BaseSpecDto();
        baseSpecDto.setSpecName(apisJsonApi.getName());
        baseSpecDto.setSpecDescription(apisJsonApi.getDescription().substring(0,25) + "...");
        baseSpecDto.setSpecVersion(
                apisJsonApi.getProperties().stream().filter(f->"X-Version".equals(f.getType())).findFirst().get().getValue()
        );
        apisJsonApi.getProperties().stream()
                .filter(f->"X-Domain".equals(f.getType()))
                .findFirst()
                .ifPresentOrElse( domain -> baseSpecDto.setSpecType("DOMAIN"), () -> baseSpecDto.setSpecType("API") );
        return baseSpecDto;
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
