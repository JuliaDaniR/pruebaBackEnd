package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationInstitution;

public interface ValidationInstitution {
    void validar(DataRegistrationInstitution dataRegistrationInstitution) throws MyException;
}
