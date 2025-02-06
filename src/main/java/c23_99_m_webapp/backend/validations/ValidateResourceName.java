package c23_99_m_webapp.backend.validations;


import c23_99_m_webapp.backend.exceptions.DuplicateResourceException;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import c23_99_m_webapp.backend.services.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateResourceName implements ValidationResource{

    ResourceRepository resourceRepository;
    UserService userService;
    public ValidateResourceName(ResourceRepository resourceRepository, UserService userService){
        this.resourceRepository = resourceRepository;
        this.userService = userService;

    }

    @Override
    public void validate(ResourceCreateDTO resource){
        User user = userService.getCurrentUser();
        List<Resource> currentInventoryResources = user.getInstitution().getInventory().getResources();

        for (Resource resourceFound: currentInventoryResources) {
            if(resourceFound.getName().equals(resource.name())){
                throw new DuplicateResourceException("El nombre del recurso ya existe en este inventario");
            }
        }
    }

}

