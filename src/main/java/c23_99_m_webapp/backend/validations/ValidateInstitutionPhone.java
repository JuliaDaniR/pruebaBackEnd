package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Institution;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationInstitution;
import c23_99_m_webapp.backend.repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateInstitutionPhone implements ValidationInstitution{
    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    public void validar(DataRegistrationInstitution dataRegistrationInstitution) throws MyException {

        Optional<Institution> optionalInstitution = institutionRepository.findByPhone(dataRegistrationInstitution.phone());
        if (optionalInstitution.isPresent()) {
            throw new MyException("Ya existe una escuela registrada con ese telefono");
        }
    }
}
