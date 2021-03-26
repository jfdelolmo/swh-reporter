package org.jfo.swaggerhub.swhreporter.repository;

import java.util.Optional;

import org.jfo.swaggerhub.swhreporter.model.db.AdminStatus;
import org.springframework.data.repository.CrudRepository;

public interface AdminStatusRepository extends CrudRepository<AdminStatus, Long> {
  
//  Optional<AdminStatus> getFirstByLastUpdateOrderByLastUpdateDesc();
  Optional<AdminStatus> findTopByOrderByLastUpdateDesc();
  
}
