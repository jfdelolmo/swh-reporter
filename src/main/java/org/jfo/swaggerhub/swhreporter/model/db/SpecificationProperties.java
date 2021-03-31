package org.jfo.swaggerhub.swhreporter.model.db;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class SpecificationProperties {

  private String type;
  private String url;
  private String defaultVersion;
  private String createdBy;
  private String versions;
  private String standardization;
  private Date created;
  private Date modified;

}
