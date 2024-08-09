package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.team.PersonCreationMemberRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamMemberDto;
import be.sgerard.neo4j.model.dto.team.TeamMemberUpdateRequestDto;
import be.sgerard.neo4j.model.team.TeamMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface TeamMemberMapper {

    TeamMemberDto mapToDto(TeamMemberEntity team);

    void fillFromDto(PersonCreationMemberRequestDto dto, @MappingTarget TeamMemberEntity member);

    void fillFromDto(TeamMemberUpdateRequestDto dto, @MappingTarget TeamMemberEntity member);
}
