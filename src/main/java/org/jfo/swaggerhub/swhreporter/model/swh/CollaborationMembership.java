package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CollaborationMembership extends CollaborationMember {

//  @JsonAdapter(RolesEnum.Adapter.class)
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

//    public static class Adapter extends TypeAdapter<RolesEnum> {
//      @Override
//      public void write(final JsonWriter jsonWriter, final RolesEnum enumeration) throws IOException {
//        jsonWriter.value(enumeration.getValue());
//      }
//
//      @Override
//      public RolesEnum read(final JsonReader jsonReader) throws IOException {
//        String value = jsonReader.nextString();
//        return RolesEnum.fromValue(String.valueOf(value));
//      }
//    }
  }

  @SerializedName("roles")
  private List<RolesEnum> roles = new ArrayList<>();

  @SerializedName("donotdisturb")
  private Boolean donotdisturb = null;

  @SerializedName("blocked")
  private Boolean blocked = null;

}

