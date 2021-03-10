package org.jfo.swaggerhub.swhreporter.mappers;

import java.time.OffsetDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.db.Properties;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonProperty;
import org.springframework.stereotype.Component;

@Component
public class SwhMapper {

  public Specification apisJsonApiToSpecModel(ApisJsonApi input) {
    Specification output = new Specification();
    output.setName(input.getName());
    int minSize = Math.min(25, input.getDescription().length());
    output.setDescription(StringUtils.isEmpty(input.getDescription()) ?
        "No description provided" :
        input.getDescription().substring(0, minSize) + "..."
    );

    output.setProperties(apiJsonPropertyToSpecPropertyModel(input.getProperties()));
    return output;
  }

  private Properties apiJsonPropertyToSpecPropertyModel(List<ApisJsonProperty> input) {
    Properties output = new Properties();

    input.forEach(p -> {
      if ("Swagger".equalsIgnoreCase(p.getType())) {
        output.setType("API");
        output.setUrl(p.getUrl());
      }
      if ("X-Domain".equalsIgnoreCase(p.getType())) {
        output.setType("DOMAIN");
        output.setUrl(p.getUrl());
      }
      if ("X-Version".equalsIgnoreCase(p.getType())) {
        output.setVersion(p.getValue());
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        output.setCreated(OffsetDateTime.parse(p.getValue()));
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        output.setModified(OffsetDateTime.parse(p.getValue()));
      }
    });
    return output;
  }
}
