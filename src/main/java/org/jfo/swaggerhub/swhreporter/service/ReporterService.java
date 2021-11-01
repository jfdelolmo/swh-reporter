package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.*;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.ProjectRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationRepository;
import org.jfo.swaggerhub.swhreporter.repository.UserRepository;
import org.jfo.swaggerhub.swhreporter.validator.SpecValidator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporterService {

    private final SpecificationRepository specificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final SpecValidator specValidator;

    private final ModelMapper modelMapper;

    public Flux<SpecInfoDto> getAllSpecifications() {
        return specificationRepository
                .findAll()
                .map(modelMapper::specModelToSpecDto)
                .defaultIfEmpty(new SpecInfoDto());
    }

    public Mono<ApiDto> getApiDetails(String id) {
        return specificationRepository
                .findById(id)
                .map(modelMapper::apiDetailsModelToDto);
    }

    public Flux<ProjectDto> getAllProjects() {
        return projectRepository
                .findAll()
                .map(modelMapper::projectModelToDto);
    }

    public Flux<ParticipantReportDto> getParticipantsReport() {
        return projectRepository
                .findAllByOrderByNameAsc()
                .map(modelMapper::projectModelToParticipantReportDto)
                .defaultIfEmpty(new ParticipantReportDto());
    }

    public Flux<InvalidSpecDto> getInvalidSpecs() {
        return specificationRepository
                .findAll()
                .map(this::callValidator)
                .filter(f -> f.getNumErrors() > 0);
    }

    private InvalidSpecDto callValidator(Specification s) {
        InvalidSpecDto ws = new InvalidSpecDto();
        if (null != s &&
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

    public Flux<UnresolvedSpecDto> getUnresolvedSpecs() {
        return specificationRepository
                .getUnresolvedSpecs()
                .map(modelMapper::specModelToUnresolvedSpecDto);
    }

    public Flux<UserDto> getAllUsers() {
        Sort sort = Sort.by(
                Sort.Order.by("role").with(Sort.Direction.DESC),
                Sort.Order.by("lastActive").with(Sort.Direction.DESC)
        );
        return userRepository.findAll(sort)
                .map(modelMapper::userToUserDto);
    }


}
