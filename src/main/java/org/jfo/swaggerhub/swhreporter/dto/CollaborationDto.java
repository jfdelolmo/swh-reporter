package org.jfo.swaggerhub.swhreporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationDto {

    private List<MemberDto> members = new ArrayList<>();
    private List<TeamDto> teams  = new ArrayList<>();

}
