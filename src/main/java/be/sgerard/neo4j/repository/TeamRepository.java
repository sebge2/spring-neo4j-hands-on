package be.sgerard.neo4j.repository;

import be.sgerard.neo4j.model.team.TeamEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.lang.NonNull;

public interface TeamRepository extends Neo4jRepository<TeamEntity, String> {

    @Query("""
            MATCH (team:Team)
            WHERE elementId(team) = $id
            OPTIONAL MATCH (team)-[composedOf:composedOf*]->(subTeam:Team)
            
            OPTIONAL MATCH (team)-[workOn:workOn*]->(project:Project)
            OPTIONAL MATCH (team)-[memberOf:memberOf*]->(person:Person)
            
            OPTIONAL MATCH (subTeam)-[workOnSub:workOn*]->(subProject:Project)
            OPTIONAL MATCH (subTeam)-[memberOfSub:memberOf*]->(subPerson:Person)
            
            DETACH DELETE team, subTeam, project, person, subProject, subPerson
            """)
    @Override
    void deleteById(@NonNull String id);
}