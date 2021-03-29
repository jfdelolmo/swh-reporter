package org.jfo.swaggerhub.swhreporter.service.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.repository.AdminReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxAdminService {

    private static final String APIKEY = "a377281d-81e5-4b5b-9391-962439111515";
    public static final String MY_OWNER = "CREALOGIX";

    private final AdminReactiveRepository adminReactiveRepository;

    public Admin createAdmin(String owner, String apiKey){
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setApikey(APIKEY);
        admin.setOwner(MY_OWNER);
        admin.setPendingToUpdate(true);

        Admin exist = adminReactiveRepository.findByOwner(MY_OWNER).block();
        if (null!=exist){
            return exist;
        }

        return adminReactiveRepository.save(admin).block();
    }

    public Mono<Admin> getMyAdmin(){
        return adminReactiveRepository.findByOwner(MY_OWNER);
    }

    public Mono<String> getMyUserOwner() {
        dummyInit();
        return adminReactiveRepository.findByOwner(MY_OWNER).map(Admin::getOwner);
    }

    //TODO: this needs to be retrieved from the DB for the current logged user
    private void dummyInit() {
        if (null==adminReactiveRepository.findByOwner(MY_OWNER).block()) {
            Admin dummyAdmin = new Admin();
            dummyAdmin.setOwner(MY_OWNER);
            dummyAdmin.setPendingToUpdate(true);
            adminReactiveRepository.save(dummyAdmin);
        }
    }

    public Mono<Boolean> myIsPendingToUpdate() {
        dummyInit();
        return adminReactiveRepository.findByOwner(MY_OWNER).map(Admin::getPendingToUpdate);
    }
}
