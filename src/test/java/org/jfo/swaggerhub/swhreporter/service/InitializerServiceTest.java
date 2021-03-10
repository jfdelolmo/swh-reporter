package org.jfo.swaggerhub.swhreporter.service;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.repository.SpecRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InitializerServiceTest {

  private final AdminService adminService = new AdminService();
  private final SwaggerHubService swaggerHubService = new SwaggerHubService(new SwhWebClient(), new ModelMapper());
  private final SwhMapper swhMapper = new SwhMapper();
  private final SpecRepository specRepository = Mockito.mock(SpecRepository.class);
  private final InitializerService initializerService = new InitializerService(adminService,
      swaggerHubService,
      swhMapper,
      specRepository);
  
  @Test
  @DisplayName("When we want to retrieve all the specifications")
  void retrieveAllSpecs() {
    int result = initializerService.retrieveAllSpecs();
    Assertions.assertThat(result).isGreaterThan(0);
  }
  
}