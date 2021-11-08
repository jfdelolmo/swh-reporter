package org.jfo.swaggerhub.swhreporter.model.swh.users;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMemberResult {

  public enum StatusEnum {
    @SerializedName("UPDATED")
    UPDATED("UPDATED"),
    @SerializedName("REMOVED")
    REMOVED("REMOVED"),
    @SerializedName("NON_MEMBER")
    NON_MEMBER("NON_MEMBER");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }
  
  
  @SerializedName("username")
  private String username;
  @SerializedName("email")
  private String email;
  @SerializedName("status")
  private StatusEnum status;
  
}
