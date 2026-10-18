package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.mapper;

import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectRequestDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectResponseDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.entity.ProjectEntity;
import java.util.List;

public interface ProjectMapper {
    ProjectResponseDto toDto(ProjectEntity projectEntity);
    List<ProjectResponseDto> toDto(List<ProjectEntity> lstProjectEntity);
    ProjectEntity toEntity(ProjectRequestDto projectDto);
}
