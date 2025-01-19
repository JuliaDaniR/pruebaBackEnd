package c23_99_m_webapp.backend.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${url.front.deploy}")
public class HealthCheckController {

    @Value("${url.front.deploy}")
    private String urlFront;

    @PostConstruct
    public void init() {
        System.out.println("URL del frontend configurada: " + urlFront);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("El servidor está funcionando correctamente");
    }
}