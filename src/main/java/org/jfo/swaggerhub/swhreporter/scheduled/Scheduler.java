package org.jfo.swaggerhub.swhreporter.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.service.message.SwhEventPayload;
import org.jfo.swaggerhub.swhreporter.service.message.SwhProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Slf4j
//@Component
//@Configuration
//@EnableScheduling
//@RequiredArgsConstructor
public class Scheduler {

//    @Value("${reporter.config.enableScheduler}")
//    private boolean enableScheduler;
//
//    private final SwhProcessor swhProcessor;
//
//    @Scheduled(initialDelay = 1000 * 60 * 1)
//    public void callProcessMyAdmin() {
//        if (enableScheduler) {
//            long startTime = System.currentTimeMillis();
//            log.info("Call for MyAdmin processor....");
//
//            SwhEventPayload payload = swhProcessor.processCallForMyAdmin();
//
//            log.info("Payload id: {}", payload.getId());
//        }
//    }


//    @Scheduled(fixedDelay = 1000 * 60 * 10)
//    public void callProcessSpecs() {
//        if (enableScheduler) {
//            long startTime = System.currentTimeMillis();
//            log.info("Call for Specs processor....");
//
//            SwhEventPayload payload = swhProcessor.processCallForSpecs();
//
//            log.info("Payload id: {}", payload.getId());
//        }
//    }


//    @Scheduled(fixedDelay = 1000 * 60)
//    public void loadSpecifications() {
//        if (enableScheduler) {
//            long startTime = System.currentTimeMillis();
//            log.info("Starting loadSpecifications....");
//      specs = initializerService.retrieveAllOwnedSpecs();
//            log.info("Data loader has finished ({} elapsed time)", System.currentTimeMillis() - startTime);
//        } else {
//            log.info("Skipped data loading");
//        }
//    }

//    @Scheduled(fixedDelay = 1000 * 60 * 60)
//    public void loadOAS() {
//        if (enableScheduler) {
//            long startTime = System.currentTimeMillis();
//            log.info("Starting loadOAS....");
//
//      specs.forEach(s->{
//        try {
//          Thread.sleep(Duration.ofSeconds(10).toMillis());
//          initializerService.retrieveApiAndStoreUpdatedSpecification(s);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//      });

//            log.info("Data loader has finished ({} elapsed time)", System.currentTimeMillis() - startTime);
//        } else {
//            log.info("Skipped data loading");
//        }
//
//    }


}
