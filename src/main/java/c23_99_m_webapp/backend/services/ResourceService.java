package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.BadCustomerRequestException;
import c23_99_m_webapp.backend.exceptions.ResourceNotFoundException;
import c23_99_m_webapp.backend.exceptions.ResourceUnavailableException;
import c23_99_m_webapp.backend.mappers.ResourceCreateMapper;
import c23_99_m_webapp.backend.mappers.ResourceViewMapper;
import c23_99_m_webapp.backend.models.*;
import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceViewDTO;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.repositories.ReservationRepository;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import c23_99_m_webapp.backend.validations.ValidationResource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static c23_99_m_webapp.backend.mappers.ResourceCreateMapper.toDTO;
import static c23_99_m_webapp.backend.mappers.ResourceCreateMapper.toEntity;
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final List<ValidationResource> validations;
    private final UserService userService;

    public ResourceService(ResourceRepository resourceRepository, ReservationRepository reservationRepository,
                           List<ValidationResource> validations, UserService userService){
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;
        this.validations = validations;
        this.userService = userService;
    }

    public List<ResourceViewDTO> getResources() {
        List<Resource> resources = resourceRepository.findAll();
        return resources.stream().map(ResourceViewMapper::toDTO).collect(Collectors.toList());

    }

    public Page<ResourceViewDTO> getPaginatedResources(Pageable pagination) {
        User user = userService.getCurrentUser();
        Institution institution = user.getInstitution();
        if (institution == null) {
            throw new ResourceNotFoundException("El usuario no tiene una institución asociada.");
        }

        Inventory inventory = institution.getInventory();
        if (inventory == null) {
            throw new ResourceNotFoundException("La institución no tiene un inventario asignado.");
        }

        List<Resource> resources = resourceRepository.findAllByInventory(inventory);
        Page<Resource> paginatedResources = paginateResources(resources, pagination);

        return paginatedResources.map(ResourceViewMapper::toDTO);
    }

    public ResourceViewDTO getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recurso con Id: " + id));

        return ResourceViewMapper.toDTO(resource);
    }

    public Resource saveResourceEntity(ResourceCreateDTO resourceDTO) {
        for (ValidationResource v : validations) {
            v.validate(resourceDTO);
        }
        Resource resource = toEntity(resourceDTO);
        return resourceRepository.save(resource);
    }


    public ResourceCreateDTO setInventoryId(ResourceCreateDTO dto, Long inventoryId){
        return new ResourceCreateDTO(dto.name(), dto.description(), dto.category(),
                dto.status(), inventoryId);

    }

    @Transactional
    public ResourceViewDTO updateResource(long id, ResourceCreateDTO dto) {
        Resource existingResource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recurso con Id: " + id));
//        for (ValidationResource v : validations) {
//            v.validate(dto);
//        }

        existingResource.setName(dto.name());
        existingResource.setDescription(dto.description());
        existingResource.setCategory(dto.category());
        existingResource.setStatus(dto.status());

        Resource modifiedResource = resourceRepository.save(existingResource);

        return ResourceViewMapper.toDTO(modifiedResource);
    }

    @Transactional
    public void deleteResource(long id){
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al recurso con id: " + id));

        boolean existingResourceReserve = this.reservationRepository.existsByResource(resource);

        if(existingResourceReserve){
            throw new BadCustomerRequestException("No es posible eliminar un recurso con reservas asociadas");
        }
        this.resourceRepository.deleteById(id);
    }

    @Transactional
    public ResourceViewDTO updateAndReturnResourceStatus(Long id, ResourceStatus status){
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el recurso con Id: " + id));

        resource.setStatus(status);
        resourceRepository.save(resource);

        return ResourceViewMapper.toDTO(resource);

    }

    public List<ResourceViewDTO> getResourcesByStatus(ResourceStatus status){
        List<Resource> resourcesByStatus = resourceRepository.findAllByStatus(status);
        return resourcesByStatus.stream().map(ResourceViewMapper::toDTO).collect(Collectors.toList());
    }

    public void validateResourceAvailability(Long resourceId, ReservationDto reservationDto) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado con ID: " + resourceId));

        LocalDate DTOStarDate = reservationDto.startDate();
        ReservationShiftStatus DTOShiftStatus = reservationDto.reservationShiftStatus();
        String DTOTimeSlot = reservationDto.selectedTimeSlot();

        //Trae una lista con todas las reservaciones realizadas según la fecha y el recurso
        List<Reservation> reservations = reservationRepository.findAllByStartDateAndResource(DTOStarDate, resource);

        if(reservations != null){
            for (Reservation reservation: reservations) {

                if(reservation.getReservationShiftStatus() == DTOShiftStatus){
                    if(reservation.getSelectedTimeSlot().equals(DTOTimeSlot)
                            && !resource.getStatus().equals(ResourceStatus.AVAILABLE) ){
                        throw new ResourceUnavailableException("El recurso no está disponible para reservar.");
                    }
                }
            }
        }

    }

    public void updateResourceStatus(Long resourceId, ResourceStatus status) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado con ID: " + resourceId));
        resource.setStatus(status);
        resourceRepository.save(resource);
    }

    public Page<Resource> paginateResources(List<Resource> resources, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize; //indice del primer elemento de cada pagina

        List<Resource> listaPaginada;

        if (resources.size() < startItem) {
            listaPaginada = List.of(); // Retorna una lista vacía si la página excede el tamaño
        } else {
            int toIndex = Math.min(startItem + pageSize, resources.size());//math.min: devuelve el menor,
            //por si la lista de recursos es menor a la cantidad solicitada de recursos por página
            listaPaginada = resources.subList(startItem, toIndex);
        }

        return new PageImpl<>(listaPaginada, pageable, resources.size());
    }


}
