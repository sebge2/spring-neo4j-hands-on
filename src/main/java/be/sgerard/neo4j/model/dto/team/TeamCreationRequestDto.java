package be.sgerard.neo4j.model.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(name = "Team Creation Request")
@SuperBuilder
@Getter
@Setter
public abstract class TeamCreationRequestDto {

    private final String name;
    private final String notes;
}
