package c23_99_m_webapp.backend.models.dtos;


import c23_99_m_webapp.backend.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DataRegistrationUser(

        @NotBlank
        String dni,
        @NotBlank
        String full_name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String password2,
        Role role
) {
        public static record DataUpdateUser(
                @NotBlank
                String dni,
                String full_name,
                String email,
                String password,
                String password2
        ){};
}
