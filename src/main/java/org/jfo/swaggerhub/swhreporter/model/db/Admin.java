package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document
public class Admin {

    @Id
    private UUID id = UUID.randomUUID();

    private String owner;
    private String apikey;
    private Boolean pendingToUpdate;

}
