package org.jfo.swaggerhub.swhreporter.model.db;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Properties {

//  @Id
//  @Column
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  private Long id;

//  @OneToOne(optional = false)
//  @JoinColumn(name = "specification_id", referencedColumnName = "id")
//  private Specification specificationModel;

//  @Column
  private String type;
  
//  @Column
  private String url;
  
//  @Column
  private String version;

//  @Column
  private OffsetDateTime created;

//  @Column
  private OffsetDateTime modified;

}
