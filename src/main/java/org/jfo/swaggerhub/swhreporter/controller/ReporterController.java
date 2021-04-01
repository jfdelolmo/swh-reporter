package org.jfo.swaggerhub.swhreporter.controller;

import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.ErrorDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectParticipantsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.dto.WrongReferenceReportDto;
import org.jfo.swaggerhub.swhreporter.exception.OpenAPIParseResultException;
import org.jfo.swaggerhub.swhreporter.service.reactive.RxReporterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reporter")
public class ReporterController {

    private final RxReporterService rxReporterService;

    @GetMapping("/specs")
    public String getSpecs(Model model) {
        log.info("Entering getAPis controller method");

        SpecsDto out = new SpecsDto();
        out.setSpecs(rxReporterService.getAllSpecifications().collectList().block());
        out.setNumberOfSpec(out.getSpecs().size());

        model.addAttribute("specs", out);
        return "reporter/specs";
    }

    @GetMapping("/specs/{id}")
    public String getSpecDetails(Model model, @PathVariable("id") String id) {
        log.info("Entering getSpecDetails controller method");
        model.addAttribute("api", rxReporterService.getApiDetails(id).block());
        return "reporter/api";
    }

    @GetMapping("/projects")
    public String getProjectsReport(Model model) {
        log.info("Entering ProjectsReport controller method");
        ProjectsReportDto dto = new ProjectsReportDto();
        dto.setProjects(rxReporterService.getAllProjects().collect(Collectors.toSet()).block());
        dto.setTotal(dto.getProjects().size());
        model.addAttribute("projects", dto);
        return "reporter/projects";
    }

    @GetMapping("/participants")
    public String getParticipantsReport(Model model) {
        log.info("Entering Participants Report controller method");
        ProjectParticipantsReportDto dto = new ProjectParticipantsReportDto();
        dto.setParticipants(rxReporterService.getParticipantsReport().collect(Collectors.toSet()).block());
        model.addAttribute("participants", dto);
        return "reporter/participants";
    }

    @GetMapping("/wrongreference")
    public String getWrongReferencedApis(Model model) {
        log.info("Entering Wrong Referenced Apis Report controller method");
        WrongReferenceReportDto dto = new WrongReferenceReportDto();
        dto.setWrongspecs(rxReporterService.getWrongReferencedApis().collect(Collectors.toSet()).block());
        dto.setTotal((long) dto.getWrongspecs().size());
        model.addAttribute("wrongreference", dto);
        return "/reporter/wrongreference";
    }
    
}
