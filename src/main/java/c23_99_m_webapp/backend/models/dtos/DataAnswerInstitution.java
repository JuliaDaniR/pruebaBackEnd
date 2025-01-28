package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.EducationalLevel;

public record DataAnswerInstitution(
        String cue,
        String name,
        String address,
        String email,
        String phone,
        EducationalLevel educational_level,
        String website
) {
}