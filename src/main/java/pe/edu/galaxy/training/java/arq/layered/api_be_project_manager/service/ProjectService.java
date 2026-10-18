package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.service;

import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectRequestDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDto> findAll() throws ServiceException;

    Long save(ProjectRequestDto requestDto) throws ServiceException;

}
