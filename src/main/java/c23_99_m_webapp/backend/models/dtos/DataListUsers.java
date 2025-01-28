package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.User;

public record DataListUsers(
        String dni,
        String fullName,
        String email,
        String cue) {

    public DataListUsers(User user) {
        this(user.getDni(),
                user.getFullName(),
                user.getEmail(),
                user.getInstitution() != null ? user.getInstitution().getCue() : "No asignado");
    }
}
