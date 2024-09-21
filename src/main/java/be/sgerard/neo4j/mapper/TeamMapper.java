package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.team.*;
import be.sgerard.neo4j.model.team.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TeamMemberMapper.class, ProjectMapper.class})
public interface TeamMapper {

    TeamSummaryDto mapToSummaryDto(TeamEntity team);

    TeamDto mapToDto(TeamEntity team);

    void fillFromDto(RootTeamCreationRequestDto dto, @MappingTarget TeamEntity team);

    void fillFromDto(SubTeamCreationRequestDto dto, @MappingTarget TeamEntity team);

    void fillFromDto(TeamUpdateRequestDto dto, @MappingTarget TeamEntity team);
}
