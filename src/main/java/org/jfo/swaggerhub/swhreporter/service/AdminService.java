package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.repository.AdminRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    
    public static final String MY_USER = "fern";

    private final AdminRepository adminRepository;
    
    private final ModelMapper modelMapper;
    
    public MyAdminDto createAdmin(String owner, String apiKey){
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setApikey(apiKey);
        admin.setOwner(owner);
        admin.setUser(MY_USER);
        admin.setPendingToUpdate(true);

        Admin exist = adminRepository.findByUserAndOwner(MY_USER, owner).block();
        if (null!=exist && exist.getApikey().equals(apiKey)){
            return modelMapper.adminToDto(exist);
        }

        return adminRepository.save(admin).map(modelMapper::adminToDto).block();
    }

    public MyAdminDto getMyAdmin(){
        return modelMapper.adminToDto(
            adminRepository.findByUser(MY_USER).block()
        );
    }
    
    public Mono<Boolean> myAdminIsPendingToUpdate() {
        return adminRepository.findByUser(MY_USER).map(Admin::getPendingToUpdate);
    }
}
