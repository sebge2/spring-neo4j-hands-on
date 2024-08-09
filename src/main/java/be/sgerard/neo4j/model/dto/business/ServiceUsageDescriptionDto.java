package be.sgerard.neo4j.model.dto.business;

import be.sgerard.neo4j.model.dto.company.CompanySummaryDto;
import be.sgerard.neo4j.model.dto.project.ProjectSummaryDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkDto;
import be.sgerard.neo4j.model.dto.service.ServiceDto;
import be.sgerard.neo4j.model.dto.team.TeamSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Service Usage Description")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class ServiceUsageDescriptionDto {

    private final ServiceDto service;
    private final ServiceLinkDto serviceLink;
    private final ProjectSummaryDto project;
    private final TeamSummaryDto team;
    private final CompanySummaryDto company;
}
