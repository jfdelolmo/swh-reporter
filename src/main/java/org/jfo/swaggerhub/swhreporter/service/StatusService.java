package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_DOMAIN;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.AdminStatusDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.db.Status;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationRepository;
import org.jfo.swaggerhub.swhreporter.repository.StatusRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {

  private final StatusRepository statusRepository;
  private final SpecificationRepository specificationRepository;
  private final ModelMapper modelMapper;

  public Mono<AdminStatusDto> getAdminStatus() {
    return statusRepository
        .findTopByOrderByLastUpdateDesc()
        .map(modelMapper::adminStatusToDto);
  }

  public void updateStatus() {
    updateStatus(0, 0);
  }

  public void updateStatus(int numInvalidApis, int numInvalidDomains) {
    Status status = new Status();

    Map<String, List<Specification>> map = specificationRepository.findAll()
        .collectList()
        .blockOptional()
        .orElse(Collections.emptyList())
        .stream()
        .collect(Collectors.groupingBy(s -> s.getSpecificationProperties().getType()));

    status.setTotalApis((long) map.get(TYPE_API).size());
    status.setTotalDomains((long) map.get(TYPE_DOMAIN).size());
    status.setErrorApis((long) numInvalidApis);
    status.setErrorDomains((long) numInvalidDomains);
    status.setLastUpdate(new Date());

    statusRepository.save(status).block();
  }

  public void updateInvalidSpecs(int numInvalidApis, int numInvalidDomains) {
    updateStatus(numInvalidApis, numInvalidDomains);
  }
}
