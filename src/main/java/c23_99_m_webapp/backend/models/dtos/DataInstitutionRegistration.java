package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.EducationalLevel;
import c23_99_m_webapp.backend.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataInstitutionRegistration(
        @NotBlank
        String cue,
        @NotBlank
        String name,
        @NotNull
        EducationalLevel educational_level,
        @NotBlank
        String address,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String phone,
        String website,
        @NotBlank
        String dniAdmin,
        @NotBlank
        String full_name_admin,
        @NotBlank
        @Email
        String email_admin,
        @NotBlank
        String password_admin,
        @NotBlank
        String password2_admin
) {
}
