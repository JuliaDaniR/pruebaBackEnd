package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.DuplicateResourceException;
import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.exceptions.ResourceConflictException;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidateResourceDescription implements ValidationResource {

    @Override
    public void validate(ResourceCreateDTO resource) {
        if(resource.description().length() >= 256){
            throw new ResourceConflictException("La descripción debe tener un máximo de 255 caracteres");
        }


    }
}