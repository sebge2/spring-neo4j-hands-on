package be.sgerard.neo4j.service.team;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.person.PersonEntity;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import be.sgerard.neo4j.model.team.TeamMemberEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface TeamManager {

    List<TeamEntity> findAll();

    Optional<TeamEntity> findById(String id);

    default TeamEntity findByIdOrDie(String id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no team with id [%s].".formatted(id)));
    }

    List<TeamEntity> findAllSubTeams(String parentTeamId);

    TeamEntity createRoot(Consumer<TeamEntity> initializer);

    TeamEntity createSub(String parentTeamId, Consumer<TeamEntity> initializer);

    TeamEntity update(String id, Consumer<TeamEntity> updater);

    Optional<TeamEntity> deleteById(String id);

    List<TeamMemberEntity> findAllMembers(String teamId);

    TeamMemberEntity createAddMember(String teamId, Consumer<TeamMemberEntity> memberInitializer, Consumer<PersonEntity> personInitializer);

    TeamMemberEntity updateMember(String teamId, String personId, Consumer<TeamMemberEntity> updater);

    Optional<TeamMemberEntity> deleteMember(String teamId, String personId);

    Collection<ProjectEntity> findAllProjects(String teamId);

    ProjectEntity addProject(String teamId, Consumer<ProjectEntity> initializer);

}
