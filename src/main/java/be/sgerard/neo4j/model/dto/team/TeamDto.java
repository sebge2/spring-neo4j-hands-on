package be.sgerard.neo4j.model.dto.team;

import be.sgerard.neo4j.model.dto.project.ProjectDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Schema(name = "Team")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class TeamDto {

    private final String id;
    private final String name;
    private final String notes;
    private final Set<TeamDto> subTeams;
    private final Set<TeamMemberDto> members;
    private final Set<ProjectDto> projects;

}
