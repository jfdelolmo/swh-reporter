package org.jfo.swaggerhub.swhreporter.model.db;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Objects;
import java.util.UUID;

@Data
@Document
public class Api {

    @Id
    private String id = UUID.randomUUID().toString();

    private String name;


    public Api(String name) {
        this.name = name;
    }
    
}
