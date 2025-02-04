package c23_99_m_webapp.backend.models.dtos;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int size,
        int number
){

}