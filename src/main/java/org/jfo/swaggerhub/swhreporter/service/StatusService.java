package org.jfo.swaggerhub.swhreporter.service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.jfo.swaggerhub.swhreporter.dto.AdminStatusDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.AdminStatus;
import org.jfo.swaggerhub.swhreporter.repository.AdminStatusRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {

  private final AdminStatusRepository adminStatusRepository;
  private final ModelMapper modelMapper;

  public AdminStatusDto getAdminStatus() {
    AtomicReference<AdminStatusDto> dto = new AtomicReference<>(new AdminStatusDto());

    Optional<AdminStatus> adminStatus = adminStatusRepository.findTopByOrderByLastUpdateDesc();
    adminStatus.ifPresentOrElse(
        as -> dto.set(modelMapper.adminStatusToDto(as)),
        () -> dto.set(null));
    
    return dto.get();
  }


}
