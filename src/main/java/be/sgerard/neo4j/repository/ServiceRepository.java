package be.sgerard.neo4j.repository;

import be.sgerard.neo4j.model.ServiceProjection;
import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Collection;
import java.util.List;

public interface ServiceRepository extends Neo4jRepository<ServiceEntity, String> {

    @Query("""
            CALL {
                MATCH (company:Company)-[partOf:partOf]->(mainTeam: Team)-[composedOf:composedOf*]->(subTeam:Team)-[workOn]->(project:Project)-[link:consume]->(service:Service)
                WHERE link.state IN $states AND elementId(service) IN $serviceIds AND (NOT $onlyWithDifficulties OR link.difficulties IS NOT NULL) AND (NOT $onlyWithStrategies OR link.strategy IS NOT NULL)
                RETURN project as project, subTeam as team, company as company, service as service, properties(link) as serviceLink
                UNION ALL
                MATCH (company:Company)-[partOf:partOf]->(mainTeam: Team)-[workOn]->(subProject:Project)-[link:consume]->(service:Service)
                WHERE link.state IN $states AND elementId(service) IN $serviceIds AND (NOT $onlyWithDifficulties OR link.difficulties IS NOT NULL) AND (NOT $onlyWithStrategies OR link.strategy IS NOT NULL)
                RETURN subProject as project, mainTeam as team, company as company, service as service, properties(link) as serviceLink
            }
            RETURN project, team, company, service, serviceLink
            ORDER BY service.name, project.name
            """)
    List<ServiceProjection> findServices(Collection<String> serviceIds,
                                         Collection<ServiceUsageState> states,
                                         boolean onlyWithDifficulties,
                                         boolean onlyWithStrategies);

}