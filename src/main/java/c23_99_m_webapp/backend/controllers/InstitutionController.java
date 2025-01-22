package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.dtos.*;
import c23_99_m_webapp.backend.services.InstitutionService;
import c23_99_m_webapp.backend.services.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import c23_99_m_webapp.backend.models.dtos.DataAnswerInstitution;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "${url.front.deploy}")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final InventoryService inventoryService;

    @PostMapping("/register")
    public ResponseEntity<?> registerInstitution(
            @Valid @RequestBody DataRegistrationInstitution dataInstitutionRegistration,
            UriComponentsBuilder uriComponentsBuilder) {
        try {
            Institution institution = institutionService.registerInstitution(dataInstitutionRegistration);
            DataAnswerInstitution dataAnswerInstitution = new DataAnswerInstitution(
                    institution.getName(),
                    institution.getAddress(),
                    institution.getEmail(),
                    institution.getPhone()
            );
            URI url = uriComponentsBuilder.path("/institution/{cue}").buildAndExpand(institution.getCue()).toUri();
            return ResponseEntity.created(url).body(Map.of(
                    "status", "success",
                    "message", "Institución registrada con éxito",
                    "data", dataAnswerInstitution
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateInstitution(@RequestBody @Valid DataRegistrationInstitution.DataUpdateInstitution dataUpdateInstitution) {
        try {
            DataListInstitution institution = institutionService.updateInstitution(dataUpdateInstitution);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Institución actualizada con éxito",
                    "data", institution
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/inventory")
    public ResponseEntity<InventoryDTO> getInventoryByInstitution(){
        InventoryDTO inventoryDTO = inventoryService.getInventoryByCurrentUser();
        return ResponseEntity.ok(inventoryDTO);
    }

    @PostMapping("/add-resource")
    public ResponseEntity<ResourceViewDTO> addResourceToInventory(@RequestBody ResourceCreateDTO resourceDTO) {
        ResourceViewDTO dto = inventoryService.createAndAddResourceToInventory(resourceDTO);
        return ResponseEntity.created(URI.create("/resource" + dto.id())).body(dto);
    }


}
