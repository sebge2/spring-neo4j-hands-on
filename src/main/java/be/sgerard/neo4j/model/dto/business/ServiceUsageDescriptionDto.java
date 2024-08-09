package be.sgerard.neo4j.model.dto.business;

import be.sgerard.neo4j.model.dto.company.CompanyDto;
import be.sgerard.neo4j.model.dto.project.ProjectDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkDto;
import be.sgerard.neo4j.model.dto.service.ServiceDto;
import be.sgerard.neo4j.model.dto.team.TeamDto;
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
    private final ServiceLinkDto serviceLink; // TODO load service in service link
    private final ProjectDto project;
    private final TeamDto team; // TODO team is null
    private final CompanyDto company;
}
