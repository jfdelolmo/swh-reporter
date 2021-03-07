package org.jfo.swaggerhub.swhreporter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecDto {

    private long numberOfSpec;
    private long offset;
    private ArrayList<BaseSpecDto> specs;

}
