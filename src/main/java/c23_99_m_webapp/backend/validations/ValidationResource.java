package c23_99_m_webapp.backend.validations;


import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;

public interface ValidationResource {
    void validate(ResourceCreateDTO resource);
}
