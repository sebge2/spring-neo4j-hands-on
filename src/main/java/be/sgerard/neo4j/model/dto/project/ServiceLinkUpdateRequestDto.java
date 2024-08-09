package be.sgerard.neo4j.model.dto.project;

import be.sgerard.neo4j.model.service.ServiceUsageState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Service Link Update Request")
@Builder
@Getter
@Setter
public class ServiceLinkUpdateRequestDto {

    private final ServiceUsageState state;
    private final String strategy;
    private final String objectives;
    private final String difficulties;
    private final String notes;

}
