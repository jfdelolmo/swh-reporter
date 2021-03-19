package org.jfo.swaggerhub.swhreporter.bootstrap;

import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${reporter.config.loadOnStartUp}")
  private boolean loadOnStartUp;

  private final InitializerService initializerService;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (loadOnStartUp) {
      long startTime = System.currentTimeMillis();
      log.info("Starting data loader....");
      initializerService.retrieveALl();
      log.info("Data loader has finished ({} elapsed time)", System.currentTimeMillis()-startTime);
    } else {
      log.info("Skipped data loading");
    }
  }

}
