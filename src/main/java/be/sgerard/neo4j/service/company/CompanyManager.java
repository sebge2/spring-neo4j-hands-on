package be.sgerard.neo4j.service.company;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.team.TeamEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface CompanyManager {

    List<CompanyEntity> search(String name);

    default List<CompanyEntity> search() {
        return search(null);
    }

    Optional<CompanyEntity> findById(String id);

    default CompanyEntity findByIdOrDie(String id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no company with id [%s].".formatted(id)));
    }

    CompanyEntity create(Consumer<CompanyEntity> initializer);

    CompanyEntity update(String id, Consumer<CompanyEntity> updater);

    Optional<CompanyEntity> deleteById(String id);

    TeamEntity createRootTeam(String companyId, Consumer<TeamEntity> initializer);
}
