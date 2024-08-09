package be.sgerard.neo4j.model.dto.team;

import be.sgerard.neo4j.model.dto.person.PersonDto;
import be.sgerard.neo4j.model.team.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Schema(name = "Team Member")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class TeamMemberDto {

    private final PersonDto person;
    private final List<TeamMemberRole> roles;
}
