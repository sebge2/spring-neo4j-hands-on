package be.sgerard.neo4j.model.dto.project;

import be.sgerard.neo4j.model.dto.service.ServiceDto;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(name = "Service Link")
@SuperBuilder
@Getter
@Setter
public class ServiceLinkDto {

    private final ServiceDto service;
    private final ServiceUsageState state;
    private final String strategy;
    private final String objectives;
    private final String difficulties;
    private final String notes;
}
