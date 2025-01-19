package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.ResourceStatus;

public record ResourceStatusUpdateDTO(
        ResourceStatus status
) {}
