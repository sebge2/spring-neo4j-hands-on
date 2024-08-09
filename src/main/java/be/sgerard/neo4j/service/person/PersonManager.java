package be.sgerard.neo4j.service.person;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.person.PersonEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface PersonManager {

    List<PersonEntity> findAll();

    Optional<PersonEntity> findById(String id);

    default PersonEntity findByIdOrDie(String id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no person with id [%s].".formatted(id)));
    }

    PersonEntity create(Consumer<PersonEntity> initializer);

    PersonEntity update(String id, Consumer<PersonEntity> updater);

    Optional<PersonEntity> deleteById(String id);
}
