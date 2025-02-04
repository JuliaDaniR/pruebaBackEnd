package c23_99_m_webapp.backend.services;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.dtos.DataAnswerInstitution;
import c23_99_m_webapp.backend.models.dtos.DataListInstitution;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationInstitution;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.models.enums.Role;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import c23_99_m_webapp.backend.validations.ValidationInstitution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final UserService userService;
    private final InstitutionRepository institutionRepository;
    private final EmailService emailService;
    private final InventoryService inventoryService;
    private final List<ValidationInstitution> validations;

    @Transactional
    public Institution registerInstitution(DataRegistrationInstitution dataInstitutionRegistration) throws MyException {
        validateInstitutionNotExists(dataInstitutionRegistration.cue());

        for (ValidationInstitution v : validations) {
            v.validar(dataInstitutionRegistration);
        }

        Institution institution = new Institution(dataInstitutionRegistration);

        institution.setInventory(inventoryService.createInventory(institution.getName()));
        institution = institutionRepository.save(institution);

        try {
            DataRegistrationUser registration = new DataRegistrationUser(
                    dataInstitutionRegistration.dniAdmin(),
                    dataInstitutionRegistration.full_name_admin(),
                    dataInstitutionRegistration.email_admin(),
                    dataInstitutionRegistration.password_admin(),
                    dataInstitutionRegistration.password2_admin(),
                    Role.ADMIN
            );

            User user = userService.registerUser(registration, institution);
            institution.setUsers(List.of(user));
            emailService.getEmailAdmin(user.getEmail(), user.getFullName(), registration.password(), institution.getName());
        } catch (MyException e) {
            institutionRepository.delete(institution);
            throw e;
        }
        return institution;
    }

    @Transactional
    public DataListInstitution updateInstitution(DataRegistrationInstitution.DataUpdateInstitution dataUpdateInstitution) throws MyException {
        Institution institution = institutionRepository.findByCue(dataUpdateInstitution.cue())
                .orElseThrow(() -> new MyException("La institución con el CUE proporcionado no fue encontrada."));

        institution.updateInstitution(dataUpdateInstitution);
        institutionRepository.save(institution);

        User admin = institution.getAdmin();

        return new DataListInstitution(
                institution.getCue(),
                institution.getName(),
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

    public DataAnswerInstitution returnDataInstitutionByCue(String cue) {
        Institution institution = institutionRepository.findByCue(cue)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una institución con el CUE proporcionado: " + cue));

        return new DataAnswerInstitution(
                institution.getCue(),
                institution.getName(),
                institution.getAddress(),
                institution.getEmail(),
                institution.getPhone(),
                institution.getEducationalLevel(),
                institution.getWebsite()
        );
    }

    public void deleteInstitution(String cue) {
        if (!institutionRepository.existsByCue(cue)) {
            throw new IllegalArgumentException("No se encontró una institución con el CUE proporcionado: " + cue);
        }
        institutionRepository.deleteByCue(cue);
    }
    private void validateInstitutionNotExists(String cue) throws MyException {
        if (institutionRepository.findByCue(cue).isPresent()) {
            throw new MyException("La escuela ya está registrada, realiza el login.");
        }
    }
}

