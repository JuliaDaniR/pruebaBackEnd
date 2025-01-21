package c23_99_m_webapp.backend.models;
import c23_99_m_webapp.backend.models.enums.ResourceCategory;
import c23_99_m_webapp.backend.models.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ResourceCategory category;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    @OneToMany(mappedBy = "resource")
    private List<Reservation> reservations;

    //Agregue porque faltaba la relacion
    @ManyToOne
    private Inventory inventory;


//private Integer quantity;
//    private String status;
//    private LocalDate incorporationDate;
}
