package be.sgerard.neo4j.model.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Sub Team Creation request")
@SuperBuilder
@Jacksonized
@Getter
@Setter
public class SubTeamCreationRequestDto extends TeamCreationRequestDto {
}
