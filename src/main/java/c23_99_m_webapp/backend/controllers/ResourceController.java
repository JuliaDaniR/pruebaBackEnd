package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.models.dtos.ResourceCreateDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceViewDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceStatusUpdateDTO;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import c23_99_m_webapp.backend.services.ResourceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/resource")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
public class ResourceController {

    ResourceService resourceService;
    public ResourceController(ResourceService resourceService){
        this.resourceService = resourceService;
    }


    @GetMapping
    public ResponseEntity<List<ResourceViewDTO>> getResources() {
        List<ResourceViewDTO> resources = resourceService.getResources();
        return ResponseEntity.ok(resources);
    }

//    @PostMapping
//    public ResponseEntity<ResourceViewDTO> createResource(@Valid @RequestBody ResourceCreateDTO resourceDTO){
//        ResourceViewDTO dto = resourceService.saveResource(resourceDTO);
//        return ResponseEntity.created(URI.create("/resource/" + dto.id())).body(dto);
//    }

    @PutMapping(value="{id}")
    public ResponseEntity<ResourceViewDTO> updateResource(@NotNull @PathVariable long id, @Valid @RequestBody ResourceCreateDTO dto){
        ResourceViewDTO dtoModificado = resourceService.updateResource(id, dto);
        return ResponseEntity.ok(dtoModificado);
    }


    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id){
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResourceViewDTO> updateResourceStatus(
            @PathVariable Long id,
            @RequestBody ResourceStatusUpdateDTO statusUpdateDTO) {
        ResourceViewDTO updatedResource = resourceService.updateResourceStatus(id, statusUpdateDTO.status());
        return ResponseEntity.ok(updatedResource);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ResourceViewDTO>> getResourcesByStatus(@RequestParam ResourceStatus status) {
        List<ResourceViewDTO> resources = resourceService.getResourcesByStatus(status);
        return ResponseEntity.ok(resources);
    }


}
