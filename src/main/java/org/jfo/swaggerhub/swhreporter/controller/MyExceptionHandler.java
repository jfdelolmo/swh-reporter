package org.jfo.swaggerhub.swhreporter.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jfo.swaggerhub.swhreporter.dto.ErrorDto;
import org.jfo.swaggerhub.swhreporter.exception.OpenAPIParseResultException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

  private static final String DEFAULT_ERROR_VIEW = "error";

  @ExceptionHandler(value = Exception.class)
  public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    // If the exception is annotated with @ResponseStatus rethrow it and let
    // the framework handle it - like the OrderNotFoundException example
    // at the start of this post.
    // AnnotationUtils is a Spring Framework utility class.
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) throw e;

    ErrorDto errorDto = new ErrorDto();
    errorDto.getMessages().add(e.getMessage());

    // Otherwise setup and send the user to a default error-view.
    ModelAndView mav = new ModelAndView();
    mav.addObject("errorData", errorDto);
    mav.addObject("url", req.getRequestURL());
    mav.setViewName(DEFAULT_ERROR_VIEW);
    return mav;
  }


  @ExceptionHandler(OpenAPIParseResultException.class)
  public String handleError(OpenAPIParseResultException ex, RedirectAttributes redirectAttributes) {
    ErrorDto errorDto = new ErrorDto();
    errorDto.setCause(ex.getMessage());
    errorDto.setMessages(ex.getErrors());
  
    redirectAttributes.addFlashAttribute("errorData", errorDto);
  
    return "redirect:/index";
  }

}