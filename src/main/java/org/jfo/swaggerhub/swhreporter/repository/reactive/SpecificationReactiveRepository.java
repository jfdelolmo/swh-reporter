package org.jfo.swaggerhub.swhreporter.repository.reactive;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SpecificationReactiveRepository extends ReactiveMongoRepository<Specification, Long> {

//    Mono<Page<NewSpecification>> findAll(Pageable page);

//    default Flux<List<NewSpecification>> saveOrUpdateAll(List<NewSpecification> input) {
//
//        List<NewSpecification> saved = new ArrayList<>();
//        input.forEach(item -> {
//            NewSpecification s = findByPropertiesUrl(item.getProperties().getUrl());
//            if (null != s) {
//                item.setId(s.getId());
//            }
//            if (!item.equals(s)) {
//                save(item);
//            }
//            saved.add(item);
//        });
//        return Flux.just(saved);
//    }

//    NewSpecification findByPropertiesUrl(String url);
}
