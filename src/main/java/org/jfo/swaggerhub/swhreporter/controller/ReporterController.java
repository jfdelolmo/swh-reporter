package org.jfo.swaggerhub.swhreporter.controller;

import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.service.ReporterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/reporter")
public class ReporterController {

    private final ReporterService reporterService;

    public ReporterController(ReporterService reporterService) {
        this.reporterService = reporterService;
    }

    @GetMapping("/specs")
    public String getSpecs(Model model) {
        log.info("Entering getAPis controller method");
        model.addAttribute("specs", reporterService.getSpecs());
        return "reporter/specs";
    }

    @GetMapping("/specs/{id}")
    public String getSpecDetails(Model model, @PathVariable("id") Long id) throws Exception {
        log.info("Entering getAPis controller method");
        model.addAttribute("api", reporterService.getApiDetails(id));
        return "reporter/api";
    }
}
