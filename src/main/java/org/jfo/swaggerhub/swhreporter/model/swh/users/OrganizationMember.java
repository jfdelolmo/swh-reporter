package org.jfo.swaggerhub.swhreporter.model.swh.users;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMember {

    @SerializedName("userId")
    private UUID userId;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("role")
    private SwaggerHubUserRole role;
    @SerializedName("inviteTime")
    private LocalDateTime inviteTime;
    @SerializedName("startTime")
    private LocalDateTime startTime;
    @SerializedName("lastActive")
    private LocalDateTime lastActive;

}
