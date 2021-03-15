package org.jfo.swaggerhub.swhreporter.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final InitializerService initializerService;

    public DataLoader(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.info("Executing data loader....");
//        int items = initializerService.retrieveAllSpecs();
//        log.info("Items load on boot up time: {}", items);
    }

}
