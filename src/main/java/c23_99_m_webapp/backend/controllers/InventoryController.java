package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.models.dtos.InventoryDTO;
import c23_99_m_webapp.backend.services.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@SecurityRequirement(name = "bearer-key")
public class InventoryController {

    public final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping
    ResponseEntity<InventoryDTO> getInventoryById(@RequestParam Long id){
        InventoryDTO inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
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

//    @PostMapping
//    public ResponseEntity<CreateInventoryDTO> createInventory(@Valid @RequestBody CreateInventoryDTO inventoryDTO){
//        CreateInventoryDTO dto = inventoryService.saveInventory(inventoryDTO);
//        return ResponseEntity.created(URI.create("/inventory/" + dto.id())).body(dto);
//    }





}
