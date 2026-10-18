package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.commons.State;

import java.time.LocalDate;

@Data
@Entity(name = "ProjectEntity")
@Table(name = "project")
public class ProjectEntity {

    @Id
    @Column(name ="project_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The name is requerid")
    @NotEmpty(message = "The name is requerid")
    @Size(min = 60, max = 240, message = "The name is requerid and should {min} and {max} caracters")
    @Column(name ="name", nullable = false, unique = true, length =20)
    private String name;

    @Column(name ="description", nullable = false)
    private String description;

    @Column(name ="state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name ="created_at", nullable = false)
    private LocalDate createdAt;

    @PrePersist
    void prePersit(){
        this.createdAt=LocalDate.now();
    }

}
