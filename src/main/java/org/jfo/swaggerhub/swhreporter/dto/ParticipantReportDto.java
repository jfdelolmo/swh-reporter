package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantReportDto {
  
  private String project;
  private Set<ParticipantDto> participants = new HashSet<>();
  
  public ParticipantReportDto addParticipant(ParticipantDto participantDto){
    this.participants.add(participantDto);
    return this;
  }
  
}
