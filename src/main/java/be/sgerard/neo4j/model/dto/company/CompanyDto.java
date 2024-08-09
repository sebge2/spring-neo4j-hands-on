package be.sgerard.neo4j.model.dto.company;

import be.sgerard.neo4j.model.dto.team.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Company")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class CompanyDto {

    private final String id;
    private final String name;
    private final TeamDto rootTeam;

}
