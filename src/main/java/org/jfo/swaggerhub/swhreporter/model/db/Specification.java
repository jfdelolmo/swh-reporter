package org.jfo.swaggerhub.swhreporter.model.db;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity(name = "specifications")
@AllArgsConstructor
@NoArgsConstructor
public class Specification {

  @Id
//  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
//  @Column
  private String name;
  
//  @Column
  private String description;

//  @Cascade(value = CascadeType.ALL)
  @Embedded
  private Properties properties;
  
}
