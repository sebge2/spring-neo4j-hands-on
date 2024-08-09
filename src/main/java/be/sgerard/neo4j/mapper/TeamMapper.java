package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.team.RootTeamCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.SubTeamCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamDto;
import be.sgerard.neo4j.model.dto.team.TeamUpdateRequestDto;
import be.sgerard.neo4j.model.team.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto mapToDto(TeamEntity team);

    void fillFromDto(RootTeamCreationRequestDto dto, @MappingTarget TeamEntity team);

    void fillFromDto(SubTeamCreationRequestDto dto, @MappingTarget TeamEntity team);

    void fillFromDto(TeamUpdateRequestDto dto, @MappingTarget TeamEntity team);
}
