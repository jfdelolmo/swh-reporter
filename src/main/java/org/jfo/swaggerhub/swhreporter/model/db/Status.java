package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Document
public class Status {

    @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.randomUUID();

    private Long totalApis;
    private Long totalDomains;
    private Long errorApis;
    private Long errorDomains;
    private Date lastUpdate;

}
