package org.jfo.swaggerhub.swhreporter.scheduled;

import java.time.Duration;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

import org.apache.commons.io.input.ObservableInputStream;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DataLoader {

  @Value("${reporter.config.enableScheduler}")
  private boolean enableScheduler;

  private final InitializerService initializerService;
  private Set<NewSpecification> specs = new HashSet<>();
  
  @Scheduled(fixedDelay = 1000*60)
  public void loadSpecifications() {
    if (enableScheduler) {
      long startTime = System.currentTimeMillis();
      log.info("Starting loadSpecifications....");
      specs = initializerService.retrieveAllOwnedSpecs();
      log.info("Data loader has finished ({} elapsed time)", System.currentTimeMillis()-startTime);
    } else {
      log.info("Skipped data loading");
    }
  }

  @Scheduled(fixedDelay = 1000*60*60)
  public void loadOAS(){
    if (enableScheduler) {
      long startTime = System.currentTimeMillis();
      log.info("Starting loadOAS....");
      
      specs.forEach(s->{
        try {
          Thread.sleep(Duration.ofSeconds(10).toMillis());
          initializerService.retrieveApiAndStoreUpdatedSpecification(s);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      
      log.info("Data loader has finished ({} elapsed time)", System.currentTimeMillis()-startTime);
    } else {
      log.info("Skipped data loading");
    }
    
  }
  

}
