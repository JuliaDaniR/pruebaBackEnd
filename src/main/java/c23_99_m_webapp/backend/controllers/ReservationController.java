package c23_99_m_webapp.backend.controllers;

import c23_99_m_webapp.backend.exceptions.MyException;
import c23_99_m_webapp.backend.models.dtos.DataAnswerReservation;
import c23_99_m_webapp.backend.models.dtos.ReservationDto;
import c23_99_m_webapp.backend.services.ReservationService;
import c23_99_m_webapp.backend.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/reservations")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
@CrossOrigin(origins = "${url.front.deploy}")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    // ENDPOINT CREATE CON RELACION A USER
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDto reservationDto) throws MyException {

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
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservationList = reservationService.getReservations();
        return ResponseEntity.ok().body(reservationList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ReservationDto>> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok().body(reservationService.findReservationById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<ReservationDto>> updateById(@PathVariable Long id, @RequestBody ReservationDto reservationDto) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.updateById(id, reservationDto));
    }

    @PutMapping("/delete/{id}") //borrado logico
    public void deleteReservation(@PathVariable Long id){
        reservationService.deleteReservationById(id);
    }

    @GetMapping("/alldeleted")
    public List<ReservationDto> deleted(){
        return reservationService.getDeletedReservations();
    }

    @PutMapping("/restore/{id}")
    public void restore(@PathVariable Long id){
        reservationService.restoreReservation(id);
    }
}