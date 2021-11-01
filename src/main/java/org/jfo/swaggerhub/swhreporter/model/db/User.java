package org.jfo.swaggerhub.swhreporter.model.db;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.jfo.swaggerhub.swhreporter.model.swh.users.SwaggerHubUserRole;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document
public class User {

  private UUID userId;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private SwaggerHubUserRole role;
  private LocalDateTime inviteTime;
  private LocalDateTime startTime;
  private LocalDateTime lastActive;

}
