package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
//import org.threeten.bp.OffsetDateTime;

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

