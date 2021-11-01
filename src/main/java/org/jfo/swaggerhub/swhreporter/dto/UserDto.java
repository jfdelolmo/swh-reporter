package org.jfo.swaggerhub.swhreporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfo.swaggerhub.swhreporter.model.swh.users.SwaggerHubUserRole;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {

  private UUID userId;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String role;
  private LocalDateTime inviteTime;
  private LocalDateTime startTime;
  private LocalDateTime lastActive;
  private boolean deleteCandidate;

  private Long totalUsers;
  private Long totalOwners;
  private Long totalDesigners;
  private Long totalConsumers;

}
