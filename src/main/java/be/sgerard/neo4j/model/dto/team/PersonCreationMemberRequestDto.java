package be.sgerard.neo4j.model.dto.team;

import be.sgerard.neo4j.model.dto.person.PersonCreationRequestDto;
import be.sgerard.neo4j.model.team.TeamMemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Schema(name = "Person Creation Member Request")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class PersonCreationMemberRequestDto {

    private final PersonCreationRequestDto person;
    private final List<TeamMemberRole> roles;

}
