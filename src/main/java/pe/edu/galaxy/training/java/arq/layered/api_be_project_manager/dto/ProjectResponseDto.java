package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({"id","name","description","state"})
@Data
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;
    private String state;
    private String createdAt;
}
