package org.jfo.swaggerhub.swhreporter.service;

import java.util.Optional;
import java.util.UUID;

import org.jfo.swaggerhub.swhreporter.model.db.User;
import org.jfo.swaggerhub.swhreporter.model.swh.users.OrganizationMemberResult;
import org.jfo.swaggerhub.swhreporter.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final AdminService adminService;
  private final SwaggerHubService swaggerHubService;

  public boolean deleteUser(UUID userUuid) {
    Optional<User> optionalUser = userRepository
        .findByUserId(userUuid)
        .blockOptional();
    
    if (optionalUser.isEmpty()){
      return false;
    }
    
    User user = optionalUser.get();

    Flux<OrganizationMemberResult> result = swaggerHubService.deleteMember(
        adminService.getMyAdmin().getOwner(), 
        user.getEmail()
    );

    Long notRemoved = result.map(OrganizationMemberResult::getStatus)
        .filter(status -> !OrganizationMemberResult.StatusEnum.REMOVED.equals(status))
        .count()
        .block();
    
    if (null==notRemoved || notRemoved==1L){
      return false;
    }
    
    return deleteExistingUser(user);     
  }
  
  private boolean deleteExistingUser(User user){
    userRepository.deleteByUserId(user.getUserId()).block();
    return true;
  }
}
