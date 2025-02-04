package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import jakarta.validation.constraints.NotBlank;

public record ResourceStatusUpdateDTO(
        @NotBlank
        ResourceStatus status
) {}

