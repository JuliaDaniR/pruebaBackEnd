package c23_99_m_webapp.backend.models;

import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.models.enums.ReservationShiftStatus;
import c23_99_m_webapp.backend.models.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer countElement;
    private LocalDate startDate;
    //private String startHour;

    @Enumerated(EnumType.STRING)
    private ReservationShiftStatus reservationShiftStatus;
    private String selectedTimeSlot;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private boolean deleted = false;

    public Reservation(ReservationDto reservationDto) {
        this.countElement = reservationDto.countElement();
        this.startDate = reservationDto.startDate();
        this.reservationShiftStatus = reservationDto.reservationShiftStatus();
        this.selectedTimeSlot = reservationDto.selectedTimeSlot();
    }

    public void handleReservationShiftStatus(){

        if (!isValidTimeSlot()) {
            System.out.println("El horario seleccionado no es válido para el turno " + this.reservationShiftStatus);
            return;
        }

        switch (this.reservationShiftStatus) {
            case MANANA, TARDE, NOCHE -> {
                System.out.println("Horarios de la mañana: " + this.reservationShiftStatus.getSchedule());
                System.out.println("Horario seleccionado: " + this.selectedTimeSlot);
            }
        }
    }
    public boolean isValidTimeSlot() {
        List<String> validTimeSlots = this.reservationShiftStatus.getSchedule();
        return validTimeSlots.contains(this.selectedTimeSlot);
    }

    public void handleReservationStatus() {
        switch (this.reservationStatus) {
            case CANCELLED:
                System.out.println("La reservación ha sido cancelada.");
                break;
            case CONFIRMED:
                System.out.println("La reservación ha sido confirmada.");
                break;
            case FINISHED:
                System.out.println("La reservación ha sido finalizada.");
                break;
            default:
                System.out.println("Estado de reservación desconocido.");
                break;
        }
    }
}