package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectRequestDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectResponseDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.service.ProjectService;
import java.net.URI;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll(){
        List<ProjectResponseDto> projects= projectService.findAll();
        if (projects.isEmpty()){
            return  ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(projects);
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProjectRequestDto projectDto){
        Long id= projectService.save(projectDto);
        if (isNull(id)){
            return  ResponseEntity.badRequest().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return  ResponseEntity.created(location).build();
    }

    // TODO


}
