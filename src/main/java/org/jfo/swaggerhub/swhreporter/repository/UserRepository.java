package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<Void> deleteByUserId(UUID userid);

    Mono<User> findByUserId(UUID uuid);

    default Flux<User> saveOrUpdateAll(Iterable<User> users) {
        List<User> newUsers = new ArrayList<>();
        users.forEach(s -> {
            User existent = findByUserId(s.getUserId()).block();
            if (null != existent) {
                if (!existent.equals(s)) {
                    existent.setUserId(s.getUserId());
                    existent.setUsername(s.getUsername());
                    existent.setEmail(s.getEmail());
                    existent.setFirstName(s.getFirstName());
                    existent.setLastName(s.getLastName());
                    existent.setRole(s.getRole());
                    existent.setInviteTime(s.getInviteTime());
                    existent.setStartTime(s.getStartTime());
                    existent.setLastActive(s.getLastActive());
                    newUsers.add(save(existent).block());
                }
            } else {
                newUsers.add(save(s).block());
            }
        });

        return Flux.fromIterable(newUsers);
    }

}
