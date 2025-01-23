package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.security.DataAuthenticationUser;
import c23_99_m_webapp.backend.security.DataJWTtoken;
import c23_99_m_webapp.backend.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DataJWTtoken> authenticateUser(@RequestBody DataAuthenticationUser dataAuthenticationUser) {
       Authentication authenticationToken = new UsernamePasswordAuthenticationToken(dataAuthenticationUser.email(), dataAuthenticationUser.password());
        Authentication userAuthenticated = authenticationManager.authenticate(authenticationToken);
        String tokenJWT = tokenService.generateToken((User) userAuthenticated.getPrincipal());
        DataJWTtoken response = new DataJWTtoken(tokenJWT, ((User) userAuthenticated.getPrincipal()).getFullName(),((User) userAuthenticated.getPrincipal()).getRole());
        return ResponseEntity.ok(response);
    }
}
