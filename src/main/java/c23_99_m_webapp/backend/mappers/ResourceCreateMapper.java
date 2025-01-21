package c23_99_m_webapp.backend.mappers;
import c23_99_m_webapp.backend.models.Inventory;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceViewDTO;
import c23_99_m_webapp.backend.repositories.InventoryRepository;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class ResourceCreateMapper {

    @Autowired
    private InventoryRepository inventoryRepository;

    public static Resource toEntity(ResourceCreateDTO dto){

        Resource resource = new Resource();
        Inventory inventory = inventoryRepository.getReferenceById(dto.inventoryId());

        resource.setName(dto.name());
        resource.setDescription(dto.description());
        resource.setCategory(dto.category());
        resource.setStatus(dto.status());
        resource.setInventory(inventory);
        return resource;
    }

    public static ResourceCreateDTO toDTO(Resource resource){

        if(resource == null){
            return null;
        }
        return new ResourceCreateDTO(
                resource.getName(),
                resource.getDescription(),
                resource.getCategory(),
                resource.getStatus(),
                resource.getInventory().getId()
        );
    }
}

