package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceViewDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceStatusUpdateDTO;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.services.InventoryService;
import c23_99_m_webapp.backend.services.ResourceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
public class ResourceController {

    public final InventoryService inventoryService;

    ResourceService resourceService;
    public ResourceController(ResourceService resourceService,InventoryService inventoryService){
        this.resourceService = resourceService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/allResources")
    public ResponseEntity<?> getResources() {
        List<ResourceViewDTO> resources = resourceService.getResources();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Lista de recursos obtenida con éxito",
                "data", resources
        ));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getResourcesById(@NotNull @PathVariable Long id) {
        ResourceViewDTO resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Recurso obtenido con éxito",
                "data", resource
        ));
    }

    @PostMapping("/add-resource")
    public ResponseEntity<?> addResourceToInventory(@Valid @RequestBody ResourceCreateDTO resourceDTO) {
        ResourceViewDTO dto = inventoryService.createAndAddResourceToInventory(resourceDTO);
        return ResponseEntity.created(URI.create("/resource" + dto.id())).body(Map.of(
                "status", "success",
                "message", "Recurso añadido con éxito",
                "data", dto
        ));

    }

//    @PostMapping
//    public ResponseEntity<ResourceViewDTO> createResource(@Valid @RequestBody ResourceCreateDTO resourceDTO){
//        ResourceViewDTO dto = resourceService.saveResource(resourceDTO);
//        return ResponseEntity.created(URI.create("/resource/" + dto.id())).body(dto);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateResource(@NotNull @PathVariable long id, @Valid @RequestBody ResourceCreateDTO dto){
        ResourceViewDTO dtoModificado = resourceService.updateResource(id, dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Recurso actualizado con éxito",
                "data", dtoModificado
        ));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id){
        resourceService.deleteResource(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Recurso eliminado con éxito"
        ));

    }

    @PatchMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateResourceStatus(
            @PathVariable Long id,
            @RequestBody ResourceStatusUpdateDTO statusUpdateDTO) {
        ResourceViewDTO updatedResource = resourceService.updateAndReturnResourceStatus(id, statusUpdateDTO.status());
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Estado de recurso actualizado con éxito",
                "data", updatedResource
        ));
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<?> getResourcesByStatus(@RequestParam ResourceStatus status) {
        List<ResourceViewDTO> resources = resourceService.getResourcesByStatus(status);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Lista de recursos por estado obtenida con éxito",
                "data", resources
        ));
    }
}
