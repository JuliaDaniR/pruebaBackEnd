package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataRegistrationUser;
import c23_99_m_webapp.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUserEmail implements ValidationUser<DataRegistrationUser>{
   @Autowired
   private UserRepository userRepository;

    @Override
    public void validar(DataRegistrationUser dataRegistrationUser) throws MyException {

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!dataRegistrationUser.email().matches(emailRegex)) {
            throw new MyException("El formato del email no es válido.");
        }
        if (userRepository.existsByEmail(dataRegistrationUser.email())) {
            throw new MyException("El email ya está registrado.");
        }
    }
}
