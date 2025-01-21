package c23_99_m_webapp.backend.models.dtos;


import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDto (

        @NotNull
        Integer countElement,
        @NotNull
        LocalDate startDate, //refactorizar
        ReservationShiftStatus reservationShiftStatus,
        String selectedTimeSlot,
        Long resourceid

){
}