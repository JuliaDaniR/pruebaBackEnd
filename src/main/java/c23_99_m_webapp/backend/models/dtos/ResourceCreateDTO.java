package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.Inventory;
import c23_99_m_webapp.backend.models.enums.ResourceCategory;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceCreateDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,
        @NotBlank(message = "La descripción es obligatoria")
        String description,
        @NotNull(message = "La categoría es obligatoria")
        ResourceCategory category,
        @NotNull(message = "El estado es obligatorio")
        ResourceStatus status,
        Long inventoryId
) {
}
