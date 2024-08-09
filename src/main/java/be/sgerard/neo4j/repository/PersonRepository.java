package be.sgerard.neo4j.repository;

import be.sgerard.neo4j.model.person.PersonEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonRepository extends Neo4jRepository<PersonEntity, String> {

}