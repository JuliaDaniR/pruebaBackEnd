package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.BadCustomerRequestException;
import c23_99_m_webapp.backend.exceptions.ResourceNotFoundException;
import c23_99_m_webapp.backend.mappers.ResourceMapper;
import c23_99_m_webapp.backend.models.dtos.ResourceDTO;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.dtos.ResourceStatusUpdateDTO;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.repositories.ReservationRepository;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static c23_99_m_webapp.backend.mappers.ResourceMapper.toDTO;
import static c23_99_m_webapp.backend.mappers.ResourceMapper.toEntity;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;

    public ResourceService(ResourceRepository resourceRepository, ReservationRepository reservationRepository){
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;

    }

    public List<ResourceDTO> getResources() {
        List<Resource> resources = resourceRepository.findAll();
        return resources.stream().map(ResourceMapper::toDTO).collect(Collectors.toList());

    }

    public ResourceDTO saveResource(ResourceDTO resourceDTO){
        Resource resource = resourceRepository.save(toEntity(resourceDTO));
        return toDTO(resource);

    }

    public ResourceDTO updateResource(long id, ResourceDTO dto) {
        Resource existingResource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recurso con Id: " + id));

        existingResource.setName(dto.name());
        existingResource.setDescription(dto.description());
        existingResource.setCategory(dto.category());
        existingResource.setStatus(dto.status());

        Resource modifiedResource = resourceRepository.save(existingResource);

        return toDTO(modifiedResource);
    }

    public void deleteResource(long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al recurso con id: " + id));

        boolean existingResourceReserve = this.reservationRepository.existsByResource(resource);

//        if(existingResourceReserve){
//            throw new BadCustomerRequestException("No es posible eliminar un recurso con reservas asociadas");
//        }
        this.resourceRepository.deleteById(id);
    }


    public ResourceDTO updateResourceStatus(Long id, ResourceStatus status){
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recurso con Id: " + id));

        resource.setStatus(status);
        resourceRepository.save(resource);

        return toDTO(resource);

    }


}
