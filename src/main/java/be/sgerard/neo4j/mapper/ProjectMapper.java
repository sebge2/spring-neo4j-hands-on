package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.project.ProjectCreationRequestDto;
import be.sgerard.neo4j.model.dto.project.ProjectDto;
import be.sgerard.neo4j.model.dto.project.ProjectSummaryDto;
import be.sgerard.neo4j.model.dto.project.ProjectUpdateRequestDto;
import be.sgerard.neo4j.model.project.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ServiceLinkMapper.class})
public interface ProjectMapper {

    ProjectSummaryDto mapToSummaryDto(ProjectEntity project);

    ProjectDto mapToDto(ProjectEntity project);

    void fillFromDto(ProjectCreationRequestDto dto, @MappingTarget ProjectEntity project);

    void fillFromDto(ProjectUpdateRequestDto dto, @MappingTarget ProjectEntity project);
}
