package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.models.dtos.ResourceDTO;
import c23_99_m_webapp.backend.models.dtos.ResourceStatusUpdateDTO;
import c23_99_m_webapp.backend.services.ResourceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.flywaydb.core.api.output.RepairOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/resource")
@SecurityRequirement(name = "bearer-key")
public class ResourceController {

    ResourceService resourceService;
    public ResourceController(ResourceService resourceService){
        this.resourceService = resourceService;
    }


    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getResources() {
        List<ResourceDTO> resources = resourceService.getResources();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> createResource(@Valid @RequestBody ResourceDTO resourceDTO){
        ResourceDTO dto = resourceService.saveResource(resourceDTO);
        return ResponseEntity.created(URI.create("/empleado/" + dto.id())).body(dto);
    }

    @PutMapping(value="{id}")
    public ResponseEntity<ResourceDTO> updateResource(@NotNull @PathVariable long id, @Valid @RequestBody ResourceDTO dto){
        ResourceDTO dtoModificado = resourceService.updateResource(id, dto);
        return ResponseEntity.ok(dtoModificado);
    }


    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id){
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResourceDTO> updateResourceStatus(
            @PathVariable Long id,
            @RequestBody ResourceStatusUpdateDTO statusUpdateDTO) {
        ResourceDTO updatedResource = resourceService.updateResourceStatus(id, statusUpdateDTO.status());
        return ResponseEntity.ok(updatedResource);
    }


}
