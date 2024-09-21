package be.sgerard.neo4j.model.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Project Summary")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class ProjectSummaryDto {

    private final String id;
    private final String name;
    private final String strategy;
    private final String objectives;
    private final String difficulties;
    private final String notes;
}
