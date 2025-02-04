package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.enums.Role;
import c23_99_m_webapp.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateReservationByUserRole {

    @Autowired
    UserService userService;

    public User validateByRole() throws MyException {

        User user = userService.getCurrentUser();

        if (user.getRole() == Role.ADMIN) {
            throw new MyException("No puede realizar reservas estando como ADMINISTRADOR.");
        }
        return user;
    }
}
