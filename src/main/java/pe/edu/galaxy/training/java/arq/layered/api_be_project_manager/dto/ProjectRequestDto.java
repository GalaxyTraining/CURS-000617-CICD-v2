package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectRequestDto {

    @NotNull(message = "The name is requerid")
    @NotEmpty(message = "The name is requerid")
    @Size(min = 60, max = 240, message = "The name is requerid and should {min} and {max} caracters")
    private String name;
    // TODO
    @NotEmpty(message = "The description is requerid")
    private String description;
}
