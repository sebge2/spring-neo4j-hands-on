package be.sgerard.neo4j.model.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Team Summary")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class TeamSummaryDto {

    private final String id;
    private final String name;
    private final String notes;

}
