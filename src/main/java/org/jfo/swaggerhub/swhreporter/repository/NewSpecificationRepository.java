package org.jfo.swaggerhub.swhreporter.repository;

import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface NewSpecificationRepository extends CrudRepository<NewSpecification, Long> {

    Page<NewSpecification> findAll(Pageable page);

    default List<NewSpecification> saveOrUpdateAll(List<NewSpecification> input) {

        List<NewSpecification> saved = new ArrayList<>();
        input.forEach(item -> {
            NewSpecification s = findByPropertiesUrl(item.getProperties().getUrl());
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

    NewSpecification findByPropertiesUrl(String url);

//    NewSpecification findByNameAndPropertiesVersion(String apiName, String version);
}
