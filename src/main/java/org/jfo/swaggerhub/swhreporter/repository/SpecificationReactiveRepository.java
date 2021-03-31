package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface SpecificationReactiveRepository extends ReactiveMongoRepository<Specification, String> {

    Flux<Specification> findAll();

    void saveAll(List<Specification> specifications);

//    Page<NewSpecification> findAll(Pageable page);
//
//    default Set<NewSpecification> saveOrUpdateAll(Set<NewSpecification> input) {
//        Set<NewSpecification> saved = new HashSet<>();
//        input.forEach(item -> {
//            NewSpecification s = findByName(item.getName());
//            if (null != s) {
//                item.setId(s.getId());
//            }
//            if (!item.equals(s)) {
//                save(item);
//            }
//            saved.add(item);
//        });
//        return saved;
//    }
//
//    NewSpecification findByName(String name);
}
