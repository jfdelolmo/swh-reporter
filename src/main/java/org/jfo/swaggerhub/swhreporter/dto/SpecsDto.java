package org.jfo.swaggerhub.swhreporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecsDto {

    private long numberOfSpec;
    private List<SpecInfoDto> specs;

}
