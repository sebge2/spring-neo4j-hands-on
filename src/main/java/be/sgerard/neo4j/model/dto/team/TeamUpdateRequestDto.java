package be.sgerard.neo4j.model.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Team Update Request")
@Builder
@Getter
@Setter
public class TeamUpdateRequestDto {

    private final String name;
    private final String notes;

}
