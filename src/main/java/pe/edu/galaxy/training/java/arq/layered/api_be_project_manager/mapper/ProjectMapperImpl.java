package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
//import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.commons.State;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectRequestDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.dto.ProjectResponseDto;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.entity.ProjectEntity;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.util.DateFormatUtil;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProjectMapperImpl implements ProjectMapper{

    private final ModelMapper modelMapper;

    @Override
    public ProjectResponseDto toDto(ProjectEntity projectEntity) {
         ProjectResponseDto responseDto=  modelMapper.map(projectEntity,ProjectResponseDto.class);
         responseDto.setCreatedAt(DateFormatUtil.format(projectEntity.getCreatedAt()));
         return  responseDto;
    }
    @Override
    public List<ProjectResponseDto> toDto(List<ProjectEntity> lstProjectEntity) {
        return lstProjectEntity.stream().map(this::toDto).toList();
    }

    @Override
    public ProjectEntity toEntity(ProjectRequestDto projectDto) {
        return modelMapper.map(projectDto,ProjectEntity.class);
    }

    /*
    @Override
    public ProjectEntity toEntity(ProjectRequestDto projectDto) {
        ProjectEntity entity= modelMapper.map(projectDto,ProjectEntity.class);
        entity.setState(State.GENERATED);
        return entity;
    }*/


}
