package be.sgerard.neo4j.service.team;

import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import be.sgerard.neo4j.model.team.TeamMemberEntity;
import be.sgerard.neo4j.repository.TeamRepository;
import be.sgerard.neo4j.service.person.PersonManager;
import be.sgerard.neo4j.service.project.ProjectManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TeamManagerImpl implements TeamManager {

    private final TeamRepository repository;
    private final PersonManager personManager;
    private final ProjectManager projectManager;

    @Override
    public List<TeamEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TeamEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<TeamEntity> findAllSubTeams(String parentTeamId) {
        return findByIdOrDie(parentTeamId).getSubTeams().stream()
                .sorted(Comparator.comparing(TeamEntity::getName))
                .toList();
    }

    @Override
    public TeamEntity createRoot(Consumer<TeamEntity> initializer) {
        final TeamEntity team = new TeamEntity();
        initializer.accept(team);

        return repository.save(team);
    }

    @Override
    public TeamEntity createSub(String parentTeamId, Consumer<TeamEntity> initializer) {
        final TeamEntity parentTeam = findByIdOrDie(parentTeamId);

        final TeamEntity team = new TeamEntity();

        initializer.accept(team);

        repository.save(team);

        team.setParentTeam(parentTeam);
        parentTeam.getSubTeams().add(team);

        repository.save(parentTeam);

        return team;
    }

    @Override
    public TeamEntity update(String id, Consumer<TeamEntity> updater) {
        final TeamEntity team = findByIdOrDie(id);

        updater.accept(team);

        return repository.save(team);
    }

    @Override
    public Optional<TeamEntity> deleteById(String id) {
        return findById(id)
                .map(team -> {
                    repository.deleteById(id);

                    return team;
                });
    }

    @Override
    public List<TeamMemberEntity> findAllMembers(String teamId) {
        return findByIdOrDie(teamId).getMembers().stream()
                .sorted(Comparator.comparing(l -> l.getPerson().getName()))
                .toList();
    }

    @Override
    public TeamMemberEntity createAddMember(String teamId, Consumer<TeamMemberEntity> memberInitializer, Consumer<PersonEntity> personInitializer) {
        final TeamEntity team = findByIdOrDie(teamId);

        final TeamMemberEntity role = new TeamMemberEntity();
        memberInitializer.accept(role);
        role.setPerson(personManager.create(personInitializer));

        team.getMembers().add(role);

        repository.save(team);

        return role;
    }

    @Override
    public TeamMemberEntity updateMember(String teamId, String personId, Consumer<TeamMemberEntity> updater) {
        final TeamEntity team = findByIdOrDie(teamId);

        return team.getMembers().stream()
                .filter(currentLink -> Objects.equals(currentLink.getPerson().getId(), personId))
                .findFirst()
                .map(member -> {
                    updater.accept(member);

                    repository.save(team);

                    return member;
                })
                .orElseThrow(() -> new IllegalArgumentException("The person [%s] is not a member of the team [%s].".formatted(personId, teamId)));
    }

    @Override
    public Optional<TeamMemberEntity> deleteMember(String teamId, String personId) {
        final TeamEntity team = findByIdOrDie(teamId);

        return team.getMembers().stream()
                .filter(currentLink -> Objects.equals(currentLink.getPerson().getId(), personId))
                .findFirst()
                .map(link -> {
                    team.getMembers().remove(link);

                    repository.save(team);

                    return link;
                });
    }

    @Override
    public Collection<ProjectEntity> findAllProjects(String teamId) {
        return findByIdOrDie(teamId).getProjects().stream()
                .sorted(Comparator.comparing(ProjectEntity::getName))
                .toList();
    }

    @Override
    public ProjectEntity addProject(String teamId, Consumer<ProjectEntity> initializer) {
        final TeamEntity team = findByIdOrDie(teamId);

        final ProjectEntity project = projectManager.create(initializer);

        team.getProjects().add(project);

        repository.save(team);

        return project;
    }
}