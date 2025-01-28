package c23_99_m_webapp.backend.models.dtos;

import java.util.List;

public record PageResponse<T>(

        List<DataAnswerDateReservation> content,
        int totalPages,
        long totalElements,
        int size,
        int number
){

}