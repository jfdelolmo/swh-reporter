package org.jfo.swaggerhub.swhreporter.controller;

import org.jfo.swaggerhub.swhreporter.service.reactive.RxStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
  
  private static final String ELAPSED_TIME_LOG = "IndexController :: Elapsed time {}";
  
  private final RxStatusService statusService;

  @RequestMapping({"", "/", "index", "index.html"})
  public String index(ModelMap model) {
    log.info("Call for status service....");
    long startTime = System.currentTimeMillis();
    
    model.addAttribute("status", statusService.getAdminStatus().block());
    
    log.info(ELAPSED_TIME_LOG, System.currentTimeMillis()-startTime);
    return "index";
  }

}
