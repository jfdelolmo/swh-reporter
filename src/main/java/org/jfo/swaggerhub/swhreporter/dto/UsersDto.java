package org.jfo.swaggerhub.swhreporter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UsersDto {

    private List<UserDto> users;
    private int numberOfUsers;
    private long totalOwners;
    private long totalDesigners;
    private long totalConsumers;
    private long totalDeletable;
}
