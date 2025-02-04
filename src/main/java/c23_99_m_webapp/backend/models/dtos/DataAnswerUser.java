package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.Role;

public record DataAnswerUser(String name, String email, Role role) {
}
