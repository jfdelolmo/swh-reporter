package org.jfo.swaggerhub.swhreporter.controller;

import org.jfo.swaggerhub.swhreporter.service.reactive.RxStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController{
    
    private final RxStatusService statusService;

    @RequestMapping({"", "/", "index", "index.html"})
    public String index(Model model){
        model.addAttribute("status", statusService.getAdminStatus().block());
        return "index";
    }

}
