package be.sgerard.neo4j.service.company;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import be.sgerard.neo4j.repository.CompanyRepository;
import be.sgerard.neo4j.service.team.TeamManager;
import info.debatty.java.stringsimilarity.JaroWinkler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class CompanyManagerImpl implements CompanyManager {

    private static final double MATCHING_THRESHOLD = 0.5;
    private static final JaroWinkler DISTANCE_ALGORITHM = new JaroWinkler();
    private static final BiFunction<String, String, Boolean> DISTANCE_FILTER = (first, second) -> DISTANCE_ALGORITHM.distance(first, second) <= MATCHING_THRESHOLD;

    private final CompanyRepository repository;
    private final TeamManager teamManager;

    @Override
    public List<CompanyEntity> search(String name) {
        final List<CompanyEntity> companies = repository.findAll();

        if (!hasText(name)) {
            return companies;
        }

        return companies.stream()
                .filter(company -> DISTANCE_FILTER.apply(company.getName(), name))
                .toList();
    }

    @Override
    public Optional<CompanyEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public CompanyEntity create(Consumer<CompanyEntity> initializer) {
        final CompanyEntity company = new CompanyEntity();

        initializer.accept(company);

        return repository.save(company);
    }

    @Override
    public CompanyEntity update(String id, Consumer<CompanyEntity> updater) {
        final CompanyEntity company = findByIdOrDie(id);

        updater.accept(company);

        return repository.save(company);
    }

    @Override
    public Optional<CompanyEntity> deleteById(String id) {
        return findById(id)
                .map(company -> {
                    repository.deleteById(id);

                    return company;
                });
    }

    @Override
    public TeamEntity createRootTeam(String companyId, Consumer<TeamEntity> initializer) {
        final CompanyEntity company = findByIdOrDie(companyId);

        return teamManager.createRoot(team -> {
            initializer.accept(team);

            team.setCompany(company);
        });
    }
}
