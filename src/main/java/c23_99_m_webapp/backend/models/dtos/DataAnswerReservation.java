package c23_99_m_webapp.backend.models.dtos;

import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;

public record DataAnswerReservation(

        String userName,
        LocalDate date,
        ReservationShiftStatus reservationShiftStatus,
        String selectedTimeSlot,
        String resourceName,
        ReservationStatus reservationStatus

) {
}
