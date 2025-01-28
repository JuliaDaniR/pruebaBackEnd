package c23_99_m_webapp.backend.security;

import c23_99_m_webapp.backend.models.enums.Role;

public record DataJWTtoken(String jwtToken, String name, String dni, Role role) {
}
