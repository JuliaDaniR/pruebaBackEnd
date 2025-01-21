package c23_99_m_webapp.backend.models.enums;

import lombok.Getter;
import java.util.List;

@Getter
public enum ReservationShiftStatus {
    MANANA(List.of("7-8", "8-9", "9-10", "10-11", "11-12", "12-13")),
    TARDE(List.of("13-14", "14-15", "15-16", "16-17", "17-18")),
    NOCHE(List.of("18-19", "19-20", "20-21", "21-22"));

    private final List<String> schedule;

    ReservationShiftStatus(List<String> schedule) {
        this.schedule = schedule;
    }
}
