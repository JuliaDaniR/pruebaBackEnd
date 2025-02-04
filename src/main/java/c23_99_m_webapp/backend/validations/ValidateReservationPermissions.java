package c23_99_m_webapp.backend.validations;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.Reservation;
import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateReservationPermissions {

    @Autowired
    UserService userService;

    public Reservation validateAuthority(Reservation reservation) throws MyException {

        User user = userService.getCurrentUser();

        if (!reservation.getUser().equals(user)) {
            throw new MyException("No tienes permiso para realizar esta acción.");
        }
        return reservation;
    }
}
