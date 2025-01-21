package c23_99_m_webapp.backend.models.dtos;

import java.util.List;

public record InventoryDTO (
        Long id,
        List<ResourceViewDTO> resources
){}


