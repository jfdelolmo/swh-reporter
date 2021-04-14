package org.jfo.swaggerhub.swhreporter.controller;

import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_API;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.TYPE_DOMAIN;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.InvalidSpecDto;
import org.jfo.swaggerhub.swhreporter.dto.InvalidSpecsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectParticipantsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.dto.UnresolvedReportDto;
import org.jfo.swaggerhub.swhreporter.service.ReporterService;
import org.jfo.swaggerhub.swhreporter.service.StatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private static final String ELAPSED_TIME_LOG = "ReporterController :: Elapsed time {}";

    private final ReporterService reporterService;
    private final StatusService statusService;

    @GetMapping("/specs")
    public String getSpecs(Model model) {
        log.info("Entering getSpecs controller method");
        long startTime = System.currentTimeMillis();

        SpecsDto out = new SpecsDto();
        out.setSpecs(reporterService.getAllSpecifications().collectList().block());
        out.setNumberOfSpec(out.getSpecs().size());

        model.addAttribute("specs", out);
        
        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis()-startTime);
        return "reporter/specs";
    }

    @GetMapping("/specs/{id}")
    public String getSpecDetails(Model model, @PathVariable("id") String id) {
        log.info("Entering getSpecDetails controller method");
        long startTime = System.currentTimeMillis();

        model.addAttribute("api", reporterService.getApiDetails(id).block());
        
        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis()-startTime);
        return "reporter/api";
    }

    @GetMapping("/projects")
    public String getProjectsReport(Model model) {
        log.info("Entering ProjectsReport controller method");
        long startTime = System.currentTimeMillis();
        
        ProjectsReportDto dto = new ProjectsReportDto();
        dto.setProjects(reporterService.getAllProjects().collect(Collectors.toSet()).block());
        dto.setTotal(dto.getProjects().size());
        model.addAttribute("projects", dto);

        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis()-startTime);
        return "reporter/projects";
    }

    @GetMapping("/participants")
    public String getParticipantsReport(Model model) {
        log.info("Entering Participants Report controller method");
        long startTime = System.currentTimeMillis();

        ProjectParticipantsReportDto dto = new ProjectParticipantsReportDto();
        dto.setParticipants(reporterService.getParticipantsReport().collect(Collectors.toSet()).block());
        model.addAttribute("participants", dto);

        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis() - startTime);
        return "reporter/participants";
    }

    @GetMapping("/invalid")
    public String getInvalidSpecs(Model model) {
        log.info("Entering Invalid Specs Report controller method");
        long startTime = System.currentTimeMillis();

        InvalidSpecsReportDto dto = new InvalidSpecsReportDto();
        Set<InvalidSpecDto> invalidSpecs = reporterService.getInvalidSpecs().collect(Collectors.toSet()).blockOptional().orElse(new HashSet<>());
        dto.setInvalidSpecs(invalidSpecs);
        dto.setTotal(invalidSpecs.size());
        model.addAttribute("invalid", dto);

        Map<String, List<InvalidSpecDto>> invalidSpecsMap = invalidSpecs.stream()
            .collect(Collectors.groupingBy(InvalidSpecDto::getType));
        statusService.updateInvalidSpecs(
            null != invalidSpecsMap.get(TYPE_API) ? invalidSpecsMap.get(TYPE_API).size() : 0,
            null != invalidSpecsMap.get(TYPE_DOMAIN) ? invalidSpecsMap.get(TYPE_DOMAIN).size() : 0
        );

        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis() - startTime);
        return "/reporter/invalid";
    }
    
    @GetMapping("/unresolved")
    public String getUnresolved(Model model){
        log.info("Entering Unresolved Specs Report controller method");
        long startTime = System.currentTimeMillis();

        UnresolvedReportDto dto = new UnresolvedReportDto();
        dto.setUnresolvedSpecs(reporterService.getUnresolvedSpecs().collect(Collectors.toSet()).block());
        dto.setTotal((long) dto.getUnresolvedSpecs().size());
        model.addAttribute("unresolved", dto);

        log.info(ELAPSED_TIME_LOG, System.currentTimeMillis()-startTime);
        return "/reporter/unresolved";
    }
    
}
