package c23_99_m_webapp.backend.models.dtos;


import c23_99_m_webapp.backend.models.User;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;

import java.time.LocalDate;

public record DataAnswerReserveByDniUser(

        Long reserveId,
        LocalDate date,
        ReservationShiftStatus reservationShiftStatus,
        String selectedTimeSlot,
        Long resourceId,
        String resourceName,
        ReservationStatus reservationStatus
) {
}

