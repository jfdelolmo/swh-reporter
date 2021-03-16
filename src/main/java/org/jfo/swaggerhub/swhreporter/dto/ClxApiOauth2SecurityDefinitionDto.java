package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClxApiOauth2SecurityDefinitionDto {

    private Set<String> scopes = new HashSet<>();
    private Set<String> roles = new HashSet<>();
    private Set<String> audiences = new HashSet<>();

}
