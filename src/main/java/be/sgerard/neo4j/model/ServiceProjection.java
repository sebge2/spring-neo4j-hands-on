package be.sgerard.neo4j.model;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceProjection {

    private CompanyEntity company;
    private ServiceEntity service;
    private ServiceLinkEntity serviceLink;
    private ProjectEntity project;
    private TeamEntity team;

}
