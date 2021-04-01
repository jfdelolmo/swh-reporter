package org.jfo.swaggerhub.swhreporter.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDto {
  
  private String cause;
  private List<String> messages = new ArrayList<>();
  
}
