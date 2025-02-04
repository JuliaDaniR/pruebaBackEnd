package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.enums.Role;

public record DataListUsers(
        String dni,
        String fullName,
        String email,
        String nameSchool,
        Role role) {

    public DataListUsers(User user) {
        this(user.getDni(),
                user.getFullName(),
                user.getEmail(),
                user.getInstitution() != null ? user.getInstitution().getName() : "No asignado",
                user.getRole()
        );
    }
}