package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collaboration {
  @SerializedName("owner")
  private String owner = null;

  @SerializedName("owners")
  private List<Map<String,String>> owners = null;

  @SerializedName("hint")
  private CollaborationHint hint = null;

  @SerializedName("members")
  private List<CollaborationMembership> members = null;

  @SerializedName("pendingMembers")
  private List<CollaborationMembership> pendingMembers = null;

  @SerializedName("teams")
  private List<CollaborationTeamMembership> teams = null;

}

