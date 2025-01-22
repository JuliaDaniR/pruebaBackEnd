package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.EducationalLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataRegistrationInstitution(
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
        public static record DataUpdateInstitution(
                @NotBlank
                String cue,
                String name,
                EducationalLevel educational_level,
                String address,
                String email,
                String phone,
                String website
        ){
        }
}
