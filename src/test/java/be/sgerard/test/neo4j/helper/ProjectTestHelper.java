package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import be.sgerard.neo4j.model.team.TeamEntity;
import be.sgerard.neo4j.service.project.ProjectManager;
import be.sgerard.neo4j.service.team.TeamManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class ProjectTestHelper {

    private final ProjectManager projectManager;
    private final TeamManager teamManager;
    private final IdTestHelper idTestHelper;

    public ProjectsStep projects() {
        return new ProjectsStep();
    }

    protected ProjectCreateStep forTeam(TeamEntity team) {
        return new ProjectCreateStep(projects(), team);
    }

    public class ProjectsStep {

        @SuppressWarnings("UnusedReturnValue")
        public ProjectsStep assertNumbers(int expected) {
            assertThat(projectManager.findAll()).hasSize(expected);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public ProjectsStep deleteAll() {
            projectManager.findAll().stream()
                    .map(ProjectEntity::getId)
                    .forEach(projectManager::deleteById);

            return this;
        }

        public ProjectStep loadByName(String name) {
            final String projectId = idTestHelper.getProjectId(name);
            return new ProjectStep(projects(), projectManager.findByIdOrDie(projectId));
        }
    }

    @RequiredArgsConstructor
    public class ProjectCreateStep {

        private final ProjectsStep projectsStep;
        private final TeamEntity team;
        private String name;
        private String strategy;
        private String objectives;
        private String difficulties;
        private String notes;

        public ProjectCreateStep name(String name) {
            this.name = name;
            return this;
        }

        public ProjectCreateStep strategy(String strategy) {
            this.strategy = strategy;
            return this;
        }

        public ProjectCreateStep objectives(String objectives) {
            this.objectives = objectives;
            return this;
        }

        public ProjectCreateStep difficulties(String difficulties) {
            this.difficulties = difficulties;
            return this;
        }

        public ProjectCreateStep notes(String notes) {
            this.notes = notes;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public ProjectStep save() {
            final ProjectEntity project = teamManager.addProject(
                    team.getId(),
                    proj -> proj
                            .setName(name)
                            .setStrategy(strategy)
                            .setObjectives(objectives)
                            .setDifficulties(difficulties)
                            .setNotes(notes)
            );

            idTestHelper.registerProject(name, project.getId());

            return new ProjectStep(projectsStep, project);
        }
    }

    @RequiredArgsConstructor
    public class ProjectStep {

        private final ProjectsStep projectsStep;
        private final ProjectEntity project;

        public ProjectEntity get() {
            return project;
        }

        public ProjectsStep and() {
            return projectsStep;
        }

        public ProjectStep createLinkWithService(String serviceHint,
                                                 Function<ProjectLinkCreateStep, ProjectLinkStep> initializer) {
            initializer.apply(new ProjectLinkCreateStep(this, serviceHint));

            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public ProjectsStep delete() {
            projectManager.deleteById(get().getId());

            return and();
        }
    }

    @RequiredArgsConstructor
    public class ProjectLinkCreateStep {

        private final ProjectStep projectStep;
        private final String serviceHint;

        private ServiceUsageState state;
        private String strategy;
        private String objectives;
        private String difficulties;
        private String notes;

        public ProjectLinkCreateStep state(ServiceUsageState state) {
            this.state = state;
            return this;
        }

        public ProjectLinkCreateStep strategy(String strategy) {
            this.strategy = strategy;
            return this;
        }

        public ProjectLinkCreateStep objectives(String objectives) {
            this.objectives = objectives;
            return this;
        }

        public ProjectLinkCreateStep difficulties(String difficulties) {
            this.difficulties = difficulties;
            return this;
        }

        public ProjectLinkCreateStep notes(String notes) {
            this.notes = notes;
            return this;
        }

        public ProjectLinkStep save() {
            final ServiceLinkEntity link = projectManager.createLink(
                    projectStep.get().getId(),
                    idTestHelper.getServiceId(serviceHint),
                    lk -> lk
                            .setState(state)
                            .setStrategy(strategy)
                            .setObjectives(objectives)
                            .setDifficulties(difficulties)
                            .setNotes(notes)
            );

            idTestHelper.registerServiceLink("%s - %s".formatted(projectStep.get().getName(), serviceHint), link.getId());

            return new ProjectLinkStep(projectStep, link);
        }
    }

    @RequiredArgsConstructor
    public class ProjectLinkStep {

        private final ProjectStep projectStep;
        private final ServiceLinkEntity link;

        public ProjectStep and() {
            return projectStep;
        }

        public ServiceLinkEntity get() {
            return link;
        }
    }
}
