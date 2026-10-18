package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.commons.State;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectRequestDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectResponseDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.entity.ProjectEntity;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.mapper.ProjectMapper;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.repository.ProjectRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectResponseDto> findAll() {
        return projectMapper.toDto(projectRepository.findAll());
    }

    @Override
    public Long save(ProjectRequestDto projectDto) {
        ProjectEntity projectEntity= projectMapper.toEntity(projectDto);
        projectEntity.setState(State.GENERATED);
        ProjectEntity retProjectEntity=projectRepository.save(projectEntity);
        return retProjectEntity.getId();
    }
}
