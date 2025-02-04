package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.InventoryDTO;
import c23_99_m_webapp.backend.services.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
public class InventoryController {

    public final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("/ofInstitution")
    public ResponseEntity<?> getInventoryByInstitution(){
        InventoryDTO inventoryDTO = inventoryService.getInventoryByCurrentUser();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Inventario de institución obtenida con éxito",
                "data", inventoryDTO
        ));
    }

    @GetMapping("getById/{id}")
    ResponseEntity<?> getInventoryById(@NotNull @PathVariable Long id){
        InventoryDTO inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Inventario obtenido con éxito",
                "data", inventory
        ));
    }

//    @PostMapping("/{id}/resources")
//    public ResponseEntity<Void> addResourceToInventory(@PathVariable Long id, @RequestBody ResourceDTO resourceDTO) {
//        inventoryService.addResourceToInventory(id, resourceDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

//    @PostMapping("/{id}/resources")
//    public ResponseEntity<Void> addResourceToInventory(@PathVariable Long id, @RequestParam Long idResource) {
//        inventoryService.addResourceToInventoryById(id, idResource);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }






}
