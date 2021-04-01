package org.jfo.swaggerhub.swhreporter.service.reactive;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_DOMAIN;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.AdminStatusDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.db.Status;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationReactiveRepository;
import org.jfo.swaggerhub.swhreporter.repository.StatusReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxStatusService {

    private final StatusReactiveRepository statusReactiveRepository;
    private final SpecificationReactiveRepository specificationReactiveRepository;
    private final ModelMapper modelMapper;

    public Mono<AdminStatusDto> getAdminStatus() {
        return statusReactiveRepository
                .findTopByOrderByLastUpdateDesc()
                .map(modelMapper::adminStatusToDto);
    }
    
    public void updateStatus(){

        Status status = new Status();

        Map<String, List<Specification>> map = specificationReactiveRepository.findAll()
            .collectList()
            .blockOptional()
            .orElse(Collections.emptyList())
            .stream()
            .collect(Collectors.groupingBy(s -> s.getSpecificationProperties().getType()));
        
        status.setTotalApis((long)map.get(TYPE_API).size());
        status.setTotalDomains((long)map.get(TYPE_DOMAIN).size());
        status.setErrorApis(0L);
        status.setErrorDomains(0L);        
        status.setLastUpdate(new Date());
        
        statusReactiveRepository.save(status).block();
    }
}
