package be.sgerard.neo4j.service.person;

import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PersonManagerImpl implements PersonManager {

    private final PersonRepository repository;

    @Override
    public List<PersonEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PersonEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public PersonEntity create(Consumer<PersonEntity> initializer) {
        final PersonEntity person = new PersonEntity();
        initializer.accept(person);

        return repository.save(person);
    }

    @Override
    public PersonEntity update(String id, Consumer<PersonEntity> updater) {
        final PersonEntity person = findByIdOrDie(id);

        updater.accept(person);

        return repository.save(person);
    }

    @Override
    public Optional<PersonEntity> deleteById(String id) {
        return findById(id)
                .map(person -> {
                    repository.delete(person);

                    return person;
                });
    }
}
