package be.sgerard.neo4j.repository;

import be.sgerard.neo4j.model.company.CompanyEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.lang.NonNull;

public interface CompanyRepository extends Neo4jRepository<CompanyEntity, String> {

    @Query("""
            MATCH (company:Company)
            WHERE elementId(company)=$id
            OPTIONAL MATCH (company)-[partOf:partOf*]->(team:Team)
            
            OPTIONAL MATCH (team)-[composedOf:composedOf*]->(subTeam:Team)
            
            OPTIONAL MATCH (team)-[workOn:workOn*]->(project:Project)
            OPTIONAL MATCH (project)-[link:consume*]->(service:Service)
            OPTIONAL MATCH (team)-[memberOf:memberOf*]->(person:Person)
            
            OPTIONAL MATCH (subTeam)-[workOnSub:workOn*]->(subProject:Project)
            OPTIONAL MATCH (subProject)-[linkSub:consume*]->(subService:Service)
            OPTIONAL MATCH (subTeam)-[memberOfSub:memberOf*]->(subPerson:Person)
            
            DETACH DELETE company, team, subTeam, project, person, subProject, subPerson
            """)
    @Override
    void deleteById(@NonNull String id);
}