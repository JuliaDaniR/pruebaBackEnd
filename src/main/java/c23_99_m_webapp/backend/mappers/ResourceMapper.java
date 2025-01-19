package c23_99_m_webapp.backend.mappers;

import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.dtos.ResourceDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceMapper {
    public static Resource toEntity(ResourceDTO dto){
        Resource resource = new Resource();

        resource.setName(dto.name());
        resource.setDescription(dto.description());
        resource.setCategory(dto.category());
        resource.setStatus(dto.status());
        return resource;
    }

    public static ResourceDTO toDTO(Resource resource){

        if(resource == null){
            return null;
        }
        return new ResourceDTO(
                resource.getId(),
                resource.getName(),
                resource.getDescription(),
                resource.getCategory(),
                resource.getStatus()
        );
    }
}

