package c23_99_m_webapp.backend.mappers;

import c23_99_m_webapp.backend.models.Inventory;
import c23_99_m_webapp.backend.models.dtos.InventoryDTO;

import java.util.stream.Collectors;

public class InventoryMapper {
    public static Inventory toEntity(InventoryDTO dto){
        Inventory inventory = new Inventory();
        inventory.setResources(dto.resources().stream().map(ResourceViewMapper::toEntity).collect(Collectors.toList()));
        return inventory;
    }

    public static InventoryDTO toDTO(Inventory inventory){

        if(inventory == null){
            return null;
        }
        return new InventoryDTO(
                inventory.getId(),
                inventory.getResources().stream().map(ResourceViewMapper::toDTO).collect(Collectors.toList())
        );
    }
}

