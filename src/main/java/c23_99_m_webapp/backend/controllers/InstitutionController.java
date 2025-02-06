package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.*;
import c23_99_m_webapp.backend.repositories.UserRepository;
import c23_99_m_webapp.backend.security.DataAuthenticationUser;
import c23_99_m_webapp.backend.security.DataJWTtoken;
import c23_99_m_webapp.backend.security.TokenService;
import c23_99_m_webapp.backend.services.InstitutionService;
import c23_99_m_webapp.backend.services.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import c23_99_m_webapp.backend.models.dtos.DataAnswerInstitution;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/institution")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class InstitutionController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody DataAuthenticationUser dataAuthenticationUser) {

        try {
            User user = userRepository.findByEmail(dataAuthenticationUser.email());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado.");
            }

            if (!user.isActive()) {
                throw new MyException("Usuario inactivo, contacte al administrador.");
            }

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    dataAuthenticationUser.email(), dataAuthenticationUser.password());
            Authentication userAuthenticated = authenticationManager.authenticate(authenticationToken);
            User authenticatedUser = (User) userAuthenticated.getPrincipal();

            String tokenJWT = tokenService.generateToken(authenticatedUser);
            DataJWTtoken response = new DataJWTtoken(
                    tokenJWT,
                    authenticatedUser.getFullName(),
                    authenticatedUser.getDni(),
                    authenticatedUser.getRole()
            );

            return ResponseEntity.ok(response);

        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Credenciales incorrectas"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor: " + e.getMessage()
            ));
        }
    }
}
