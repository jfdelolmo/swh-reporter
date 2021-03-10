package org.jfo.swaggerhub.swhreporter.service;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
  
  public String getUserOwner(){
    return "CREALOGIX"; //TODO: this needs to be retrieved from the DB for the current logged user
  }
}
