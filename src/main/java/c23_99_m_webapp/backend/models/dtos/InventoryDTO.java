package c23_99_m_webapp.backend.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public record InventoryDTO (
        Long id,
        List<ResourceDTO> resources
    ){}


