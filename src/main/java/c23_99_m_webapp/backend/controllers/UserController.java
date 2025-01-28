package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataAnswerUser;
import c23_99_m_webapp.backend.models.dtos.DataListUsers;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import c23_99_m_webapp.backend.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InstitutionRepository institutionRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody DataRegistrationUser dataUserRegistration,
                                          UriComponentsBuilder uriComponentsBuilder) {
        try {
            User userAutenticated = userService.getCurrentUser();
            Optional<Institution> institutionOptional = institutionRepository.findByCue(userAutenticated.getInstitution().getCue());
            User user = userService.registerUser(dataUserRegistration,institutionOptional.get());
            DataAnswerUser dataAnswerUser = new DataAnswerUser(user.getFullName(), user.getEmail());
            URI url = uriComponentsBuilder.path("/user/{dni}").buildAndExpand(user.getDni()).toUri();
            return ResponseEntity.created(url).body(Map.of(
                    "status", "success",
                    "message", "Usuario registrado con éxito",
                    "data", dataAnswerUser
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> listUsers(@PageableDefault(size = 9) Pageable pagination) {
        try {
            Page<DataListUsers> users = userService.listUsers(pagination);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuarios obtenidos con éxito",
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al obtener la lista de usuarios"
            ));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid DataRegistrationUser.DataUpdateUser dataUserUpdate) {
        try {
            DataListUsers user = userService.updateUser(dataUserUpdate);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario actualizado con éxito",
                    "data", user
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al actualizar el usuario"
            ));
        }
    }

    @PutMapping("/deactivate/{dni}")
    public ResponseEntity<?> deactivateUser(@PathVariable String dni) {
        try {
            userService.deactivateUser(dni);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario desactivado con éxito"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al desactivar el usuario"
            ));
        }
    }

    @PutMapping("/activate/{dni}")
    public ResponseEntity<?> activateUser(@PathVariable String dni) {
        try {
            userService.activateUser(dni);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario activado con éxito"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al activar el usuario"
            ));
        }
    }

    @DeleteMapping("/delete/{dni}")
    public ResponseEntity<?> deleteUser(@PathVariable String dni) {
        try {
            userService.deleteUser(dni);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario eliminado con éxito"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al eliminar el usuario"
            ));
        }
    }

    @GetMapping("/getDni/{dni}")
    public ResponseEntity<?> returnDataUserByDni(@PathVariable String dni) {
        try {
            DataListUsers dataUser = userService.returnDataUserByDni(dni);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario obtenido con éxito",
                    "data", dataUser
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al obtener el usuario"
            ));
        }
    }

    @GetMapping("/getName/{fullName}")
    public ResponseEntity<?> returnDataUserByName(@PathVariable String fullName) {
        try {
            DataListUsers dataUser = userService.returnDataUserByName(fullName);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario obtenido con éxito",
                    "data", dataUser
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno al obtener el usuario"
            ));
        }
    }
}
