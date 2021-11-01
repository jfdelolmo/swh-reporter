package org.jfo.swaggerhub.swhreporter.model.swh.users;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationMembersResult {

    @SerializedName("totalCount")
    private Integer totalCount;
    @SerializedName("pageSize")
    private Integer pageSize;
    @SerializedName("page")
    private Integer page;
    @SerializedName("items")
    private List<OrganizationMember> items;

}
