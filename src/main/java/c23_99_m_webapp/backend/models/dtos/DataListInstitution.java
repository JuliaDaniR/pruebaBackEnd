package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.EducationalLevel;

public record DataListInstitution(
        String cue,
        String name,
        EducationalLevel educational_level,
        String address,
        String email,
        String phone,
        String website,
        String dniAdmin,
        String full_name_admin,
        String email_admin
) {
}
