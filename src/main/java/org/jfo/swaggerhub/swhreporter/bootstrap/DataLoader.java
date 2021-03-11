package org.jfo.swaggerhub.swhreporter.bootstrap;

import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoader implements ApplicationRunner {
  
  private final InitializerService initializerService;

  public DataLoader(InitializerService initializerService) {
    this.initializerService = initializerService;
  }

  @Override
  public void run(ApplicationArguments args)  {
    //log.info("Executing data loader....");
    //int items = initializerService.retrieveAllSpecs();
    //log.info("Items load on boot up time: {}", items);
  }
}
