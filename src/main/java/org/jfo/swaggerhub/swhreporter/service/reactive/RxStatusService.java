package org.jfo.swaggerhub.swhreporter.service.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.AdminStatusDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.repository.StatusReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxStatusService {

    private final StatusReactiveRepository statusReactiveRepository;
    private final ModelMapper modelMapper;

    public Mono<AdminStatusDto> getAdminStatus() {

        return statusReactiveRepository
                .findTopByOrderByLastUpdateDesc()
                .map(modelMapper::adminStatusToDto);
    }
}
