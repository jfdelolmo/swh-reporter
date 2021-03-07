package org.jfo.swaggerhub.swhreporter.model.swh;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApisJson {

    @SerializedName("name")
    private String name = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("url")
    private String url = null;

    @SerializedName("offset")
    private Integer offset = null;

    @SerializedName("totalCount")
    private Long totalCount = null;

    @SerializedName("apis")
    private List<ApisJsonApi> apis = null;

}

