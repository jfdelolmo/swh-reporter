package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationMember {
  @SerializedName("uuid")
  private String uuid = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("startTime")
  private OffsetDateTime startTime = null;

}