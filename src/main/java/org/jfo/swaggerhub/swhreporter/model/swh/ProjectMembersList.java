package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMembersList {

  @SerializedName("members")
  private List<ProjectMember> members;
  
}
