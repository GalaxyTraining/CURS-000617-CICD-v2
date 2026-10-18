package pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.training.java.arq.layered.api_be_project_manager.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {


}
