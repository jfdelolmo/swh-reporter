package org.jfo.swaggerhub.swhreporter.repository;

import java.util.ArrayList;
import java.util.List;

import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecRepository extends JpaRepository<Specification, Long> {
  
  Specification findByPropertiesUrl(String url);
  
  default List<Specification> saveOrUpdateAll(List<Specification> input){

    List<Specification> saved = new ArrayList<>();
    input.forEach(item -> {
      Specification s = findByPropertiesUrl(item.getProperties().getUrl());
      if (null != s){
        item.setId(s.getId());
      }
      save(item);
      saved.add(item);
    });
    return saved;
  }
  
  
}
