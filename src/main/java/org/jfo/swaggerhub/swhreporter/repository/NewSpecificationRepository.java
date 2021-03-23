package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.Set;

public interface NewSpecificationRepository extends CrudRepository<NewSpecification, Long> {

    Page<NewSpecification> findAll(Pageable page);

    default Set<NewSpecification> saveOrUpdateAll(Set<NewSpecification> input) {
        Set<NewSpecification> saved = new HashSet<>();
        input.forEach(item -> {
            NewSpecification s = findByName(item.getName());
            if (null != s) {
                item.setId(s.getId());
            }
            if (!item.equals(s)) {
                save(item);
            }
            saved.add(item);
        });
        return saved;
    }

    NewSpecification findByName(String name);

}
