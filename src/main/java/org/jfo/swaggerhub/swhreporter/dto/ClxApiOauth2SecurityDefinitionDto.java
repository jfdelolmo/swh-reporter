package org.jfo.swaggerhub.swhreporter.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClxApiOauth2SecurityDefinitionDto {

    private Set<String> scopes;
    private Set<String> roles;
    private Set<String> audiences;

}
