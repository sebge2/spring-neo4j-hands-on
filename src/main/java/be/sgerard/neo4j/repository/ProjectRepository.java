package be.sgerard.neo4j.repository;

import be.sgerard.neo4j.model.project.ProjectEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProjectRepository extends Neo4jRepository<ProjectEntity, String> {

}