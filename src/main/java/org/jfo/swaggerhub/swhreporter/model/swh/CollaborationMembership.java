package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CollaborationMembership extends CollaborationMember {

  public enum RolesEnum {
    @SerializedName("EDIT")
    EDIT("EDIT"),
    @SerializedName("COMMENT")
    COMMENT("COMMENT"),
    @SerializedName("VIEW")
    VIEW("VIEW");

    private String value;

    RolesEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RolesEnum fromValue(String text) {
      for (RolesEnum b : RolesEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }

  @SerializedName("roles")
  private List<RolesEnum> roles = new ArrayList<>();

  @SerializedName("donotdisturb")
  private Boolean donotdisturb = null;

  @SerializedName("blocked")
  private Boolean blocked = null;

}

