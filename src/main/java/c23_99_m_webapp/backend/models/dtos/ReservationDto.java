package c23_99_m_webapp.backend.models.dtos;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDto (

        @NotNull
        Integer countElement,
        @NotNull
        LocalDate startDate, //refactorizar
        @NotNull
        String starHour,
        Long resourceid

){
}
