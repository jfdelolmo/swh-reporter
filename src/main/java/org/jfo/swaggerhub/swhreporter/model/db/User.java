package org.jfo.swaggerhub.swhreporter.model.db;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jfo.swaggerhub.swhreporter.model.swh.users.SwaggerHubUserRole;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof User)) return false;

    User user = (User) o;

    return new EqualsBuilder().append(userId, user.userId).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(userId).toHashCode();
  }
}
