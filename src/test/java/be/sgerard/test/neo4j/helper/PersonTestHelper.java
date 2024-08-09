package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.service.person.PersonManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class PersonTestHelper {

    private final PersonManager personManager;
    private final IdTestHelper idTestHelper;

    public PersonsStep persons() {
        return new PersonsStep();
    }

    public class PersonsStep {

        @SuppressWarnings("UnusedReturnValue")
        public PersonsStep assertNumbers(int expected) {
            assertThat(personManager.findAll()).hasSize(expected);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public PersonsStep deleteAll() {
            personManager.findAll().stream()
                    .map(PersonEntity::getId)
                    .forEach(personManager::deleteById);

            return this;
        }

        public PersonStep loadByName(String hint) {
            return new PersonStep(this, personManager.findByIdOrDie(idTestHelper.getPersonId(hint)));
        }
    }

    @RequiredArgsConstructor
    public class PersonStep {

        private final PersonsStep personsStep;
        private final PersonEntity person;

        public PersonsStep and() {
            return personsStep;
        }

        public PersonEntity get() {
            return person;
        }

        public PersonsStep delete() {
            personManager.deleteById(person.getId());
            return and();
        }
    }
}
