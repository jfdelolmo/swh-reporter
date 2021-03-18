package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {
  
  @SerializedName("name")
  private String name;
  @SerializedName("type")
  private TypeEnum type;
  @SerializedName("roles")
  private List<ProjectMember.RolesEnum> roles = new ArrayList<>();

  public enum TypeEnum {
    @SerializedName("USER")
    USER("USER"),
    @SerializedName("TEAM")
    TEAM("TEAM");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ProjectMember.TypeEnum fromValue(String text) {
      for (ProjectMember.TypeEnum b : ProjectMember.TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  public enum RolesEnum {
    @SerializedName("MEMBER")
    MEMBER("MEMBER"),
    @SerializedName("OWNER")
    OWNER("OWNER");

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

    public static ProjectMember.RolesEnum fromValue(String text) {
      for (ProjectMember.RolesEnum b : ProjectMember.RolesEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
}
