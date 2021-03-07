package org.jfo.swaggerhub.swhreporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiDto {

    private String name;
    private String version;
    private String document;
    private String project;
    private CollaborationDto collaboration;
    private ArrayList<SecurityDefinition> securityDefinitions;

}
