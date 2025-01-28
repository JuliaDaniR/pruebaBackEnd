package c23_99_m_webapp.backend.models;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationInstitution;
import c23_99_m_webapp.backend.models.enums.EducationalLevel;
import c23_99_m_webapp.backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "institutions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Institution {

    @Id
    private String cue;
    private String name;
    @Enumerated(EnumType.STRING)
    private EducationalLevel educationalLevel;
    private String address;
    private String email;
    private String phone;
    private String website;
    @OneToMany(mappedBy = "institution")
    private List<User> users;
    @OneToOne
    private Inventory inventory;

    public Institution(DataRegistrationInstitution dataInstitutionRegistration) {
        this.cue = dataInstitutionRegistration.cue();
        this.name = dataInstitutionRegistration.name();
        this.educationalLevel = dataInstitutionRegistration.educational_level();
        this.address = dataInstitutionRegistration.address();
        this.email = dataInstitutionRegistration.email();
        this.phone = dataInstitutionRegistration.phone();
        this.website = dataInstitutionRegistration.website();
    }

    public void updateInstitution(DataRegistrationInstitution.DataUpdateInstitution dataUpdateInstitution) {
        if (dataUpdateInstitution.name() != null) {
            this.name = dataUpdateInstitution.name();
        }
        if (dataUpdateInstitution.educational_level() != null) {
            this.educationalLevel = dataUpdateInstitution.educational_level();
        }
        if (dataUpdateInstitution.address() != null) {
            this.address = dataUpdateInstitution.address();
        }
        if (dataUpdateInstitution.email() != null) {
            this.email = dataUpdateInstitution.email();
        }
        if (dataUpdateInstitution.phone() != null) {
            this.phone = dataUpdateInstitution.phone();
        }
        if (dataUpdateInstitution.website() != null) {
            this.website = dataUpdateInstitution.website();
        }
    }

    public User getAdmin() throws MyException {
        return users.stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .findFirst()
                .orElseThrow(() -> new MyException("Administrador no encontrado para esta institución."));
    }
}
