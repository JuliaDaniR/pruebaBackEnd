package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.exceptions.ResourceNotFoundException;
import c23_99_m_webapp.backend.mappers.InventoryMapper;
import c23_99_m_webapp.backend.mappers.ResourceCreateMapper;
import c23_99_m_webapp.backend.mappers.ResourceViewMapper;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.Inventory;
import c23_99_m_webapp.backend.models.Resource;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.InventoryDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceViewDTO;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import c23_99_m_webapp.backend.repositories.InventoryRepository;
import c23_99_m_webapp.backend.repositories.ResourceRepository;
import c23_99_m_webapp.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class InventoryService {

    public final InventoryRepository inventoryRepository;
    public final ResourceService resourceService;
    public final UserService userService;

    public InventoryService(InventoryRepository inventoryRepository,ResourceService resourceService,
                            UserService userService){
        this.inventoryRepository = inventoryRepository;
        this.resourceService = resourceService;
        this.userService = userService;
    }

    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el inventario con id: " + id));
        return InventoryMapper.toDTO(inventory);
    }

    public InventoryDTO getInventoryByCurrentUser() throws MyException {

        User user = userService.getCurrentUser();

        Institution institution = user.getInstitution();
        if (institution == null) {
            throw new ResourceNotFoundException("El usuario no tiene una institución asociada.");
        }

        Inventory inventory = institution.getInventory();
        if (inventory == null) {
            throw new ResourceNotFoundException("La institución no tiene un inventario asignado.");
        }

        return InventoryMapper.toDTO(inventory);
    }

    public ResourceViewDTO createAndAddResourceToInventory(ResourceCreateDTO resourceDTO) throws MyException {

        User user = userService.getCurrentUser();

        Institution institution = user.getInstitution();
        if (institution == null) {
            throw new ResourceNotFoundException("El usuario no tiene una institución asociada.");
        }

        Inventory inventory = institution.getInventory();
        if (inventory == null) {
            throw new ResourceNotFoundException("La institución no tiene un inventario asignado.");
        }

        resourceDTO = resourceService.setInventoryId(resourceDTO, inventory.getId());
        Resource savedResource = resourceService.saveResourceEntity(resourceDTO);

        inventory.getResources().add(savedResource);

        inventoryRepository.save(inventory);

        return ResourceViewMapper.toDTO(savedResource);
    }


    public Inventory createInventory(){
        Inventory inventory = new Inventory();
        inventory.setResources(new ArrayList<>());

        inventoryRepository.save(inventory);

        return inventory;

    }


}

