package org.jfo.swaggerhub.swhreporter.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
  
  public String getUserOwner(){
    return "CREALOGIX"; //TODO: this needs to be retrieved from the DB for the current logged user
  }
  
  
}
