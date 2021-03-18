package org.jfo.swaggerhub.swhreporter.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectParticipantsReportDto {
  
  private Set<ParticipantReportDto> participants = new HashSet<>();
  
  public ProjectParticipantsReportDto addParticipant(ParticipantReportDto participantDto){
    this.participants.add(participantDto);
    return this;
  }
  
}
