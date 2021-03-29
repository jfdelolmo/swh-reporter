package org.jfo.swaggerhub.swhreporter.service.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.ProjectReactiveRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationReactiveRepository;
import org.jfo.swaggerhub.swhreporter.validator.SpecValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxReporterService {

    private final SpecificationReactiveRepository specificationReactiveRepository;
    private final ProjectReactiveRepository projectReactiveRepository;

    private final SpecValidator specValidator;

    private final ModelMapper modelMapper;

    public Flux<SpecInfoDto> getAllSpecifications() {
        return specificationReactiveRepository
                .findAll()
                .map(modelMapper::specModelToSpecDto)
                .defaultIfEmpty(new SpecInfoDto());
    }

    public Mono<ApiDto> getApiDetails(UUID id) {
        return specificationReactiveRepository
                .findById(id)
                .map(modelMapper::apiDetailsModelToDto);
    }

    public Flux<ProjectDto> getAllProjects() {
        return projectReactiveRepository
                .findAll()
                .map(modelMapper::projectModelToDto);
    }


    public Flux<ParticipantReportDto> getParticipantsReport() {
        return projectReactiveRepository
                .findAllByOrderByNameAsc()
                .map(modelMapper::projectModelToParticipantReportDto)
                .defaultIfEmpty(new ParticipantReportDto());
    }

    public Flux<WrongReferenceSpecDto> getWrongReferencedApis() {
        return specificationReactiveRepository
                .findAll()
                .map(this::callValidator);
    }

    private WrongReferenceSpecDto callValidator(Specification s) {
        WrongReferenceSpecDto ws = new WrongReferenceSpecDto();
        if (null != s.getSpecificationProperties() &&
                null != s.getSpecificationProperties() &&
                null != s.getOpenApiDocument()) {
            ws = modelMapper.validations(
                    s,
                    specValidator.validate(
                            s.getOpenApiDocument().getUnresolved(),
                            s.getSpecificationProperties().getType()));
        }
        return ws;
    }
}
