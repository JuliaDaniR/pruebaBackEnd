package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.ResourceCategory;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import jakarta.validation.constraints.NotBlank;


public record ResourceViewDTO(

        Long id,
        String name,
        String description,
        ResourceCategory category,
        ResourceStatus status,
        Long inventoryId
){
}
