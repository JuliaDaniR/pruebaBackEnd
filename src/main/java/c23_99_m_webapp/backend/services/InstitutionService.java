package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataListInstitution;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationInstitution;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.models.enums.Role;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final UserService userService;
    private final InstitutionRepository institutionRepository;

    @Transactional
    public Institution registerInstitution(DataRegistrationInstitution dataInstitutionRegistration) throws MyException {
        Optional<Institution> optionalInstitution = institutionRepository.findByCue(dataInstitutionRegistration.cue());

        if (optionalInstitution.isPresent()) {
            throw new MyException("La escuela ya está registrada, realiza el login.");
        }

        Institution institution = new Institution(dataInstitutionRegistration);

        institutionRepository.save(institution);

        DataRegistrationUser registration = new DataRegistrationUser(
                dataInstitutionRegistration.dniAdmin(),
                dataInstitutionRegistration.full_name_admin(),
                dataInstitutionRegistration.email_admin(),
                dataInstitutionRegistration.password_admin(),
                dataInstitutionRegistration.password2_admin(),
                Role.ADMIN,
                institution.getCue()
        );

        User user;
        try {
            user = userService.registerUser(registration);
        } catch (MyException e) {
            throw new MyException("Error al registrar el usuario administrador: " + e.getMessage());
        }

        institution.setUsers(List.of(user));

        return institutionRepository.save(institution);
    }
    @Transactional
    public DataListInstitution updateInstitution(DataRegistrationInstitution.DataUpdateInstitution dataUpdateInstitution) throws MyException {

        Optional<Institution> optionalInstitution = institutionRepository.findByCue(dataUpdateInstitution.cue());
        if (optionalInstitution.isEmpty()) {
            throw new MyException("La institución con el CUE proporcionado no fue encontrada.");
        }
        Institution institution = optionalInstitution.get();
        institution.updateInstitution(dataUpdateInstitution);
        institutionRepository.save(institution);

        User admin = institution.getUsers().stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .findFirst()
                .orElseThrow(() -> new MyException("Administrador no encontrado para esta institución."));

        return new DataListInstitution(
                institution.getCue(),
                institution.getName(),
                institution.getTypeInstitution(),
                institution.getEducationalLevel(),
                institution.getAddress(),
                institution.getEmail(),
                institution.getPhone(),
                institution.getWebsite(),
                admin.getDni(),
                admin.getFullName(),
                admin.getEmail()
        );
    }
}
