package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataAnswerDateReservation;
import c23_99_m_webapp.backend.models.dtos.DataAnswerReservation;
import c23_99_m_webapp.backend.models.dtos.PageResponse;
import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.services.ReservationService;
import c23_99_m_webapp.backend.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping(value = "/reservations")
@CrossOrigin(origins = "https://class-kit.vercel.app", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDto reservationDto) {

        try {
            DataAnswerReservation reservationDto1 = reservationService.createdReservation(reservationDto);

            return ResponseEntity.ok(Map.of("status",
                    "success", "message",
                    "Reserva creada con éxito","data",
                    reservationDto1));

        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status",
                    "error", "message",
                    "Error al crear la reserva."));
        }
    }

    @GetMapping("/allreservations")
    public ResponseEntity<List<ReservationDto>> getAllReservations() throws MyException {

        List<ReservationDto> reservationList = reservationService.getReservations();
        return ResponseEntity.ok().body(reservationList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ReservationDto>> getReservationById(@PathVariable Long id) throws MyException {
        return ResponseEntity.ok().body(reservationService.findReservationById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<ReservationDto>> updateById(@PathVariable Long id, @RequestBody ReservationDto reservationDto) throws MyException {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateById(id, reservationDto));
    }

    @PutMapping("/delete/{id}") //borrado logico
    public void deleteReservation(@PathVariable Long id) throws MyException {
        reservationService.deleteReservationById(id);
    }

    @GetMapping("/alldeleted")
    public List<ReservationDto> deleted(){
        return reservationService.getDeletedReservations();
    }

    @PutMapping("/restore/{id}")
    public void restore(@PathVariable Long id) {
        reservationService.restoreReservation(id);
    }

    // filtrado por fechas con paginacion
    @GetMapping("/forDate/{date}")
    public ResponseEntity<?> listForDate(@PathVariable("date") LocalDate startDate,
                                         @PageableDefault(size = 5) Pageable pageable) {
        try {
            Page<DataAnswerDateReservation> reservationDtoPage = reservationService.findByDate(startDate, pageable);
            PageResponse<DataAnswerDateReservation> response = new PageResponse<>(
                    reservationDtoPage.getContent(), //->aca deberian verse los datos de la reserva
                    reservationDtoPage.getTotalPages(),
                    reservationDtoPage.getTotalElements(),
                    reservationDtoPage.getSize(),
                    reservationDtoPage.getNumber()
            );

            return ResponseEntity.ok(Map.of("status", "success",
                    "message", "Filtrado realizado con éxito.",
                    "data", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error",
                    "message", "Error interno al obtener la lista de reservas."));
        }
    }

}
