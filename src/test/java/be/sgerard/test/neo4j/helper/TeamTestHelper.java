package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.team.TeamEntity;
import be.sgerard.neo4j.model.team.TeamMemberEntity;
import be.sgerard.neo4j.model.team.TeamMemberRole;
import be.sgerard.neo4j.service.company.CompanyManager;
import be.sgerard.neo4j.service.team.TeamManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class TeamTestHelper {

    private final TeamManager manager;
    private final CompanyManager companyManager;
    private final ProjectTestHelper projectTestHelper;
    private final IdTestHelper idTestHelper;

    protected RootTeamCreateStep forRoot(CompanyEntity company) {
        return new RootTeamCreateStep(company);
    }

    public TeamsStep teams() {
        return new TeamsStep();
    }

    @RequiredArgsConstructor
    public class TeamsStep {

        @SuppressWarnings("UnusedReturnValue")
        public TeamsStep assertNumbers(int expected) {
            assertThat(manager.findAll()).hasSize(expected);

            return this;
        }

        public RootTeamStep loadRootByName(String hint) {
            final String teamId = idTestHelper.getTeamId(hint);
            final TeamEntity team = manager.findByIdOrDie(teamId);

            return new RootTeamStep(team);
        }

        @SuppressWarnings("unused")
        public SubTeamStep loadSubByName(String hint) {
            final String teamId = idTestHelper.getTeamId(hint);
            final TeamEntity team = manager.findByIdOrDie(teamId);

            return new SubTeamStep(team);
        }
    }

    @RequiredArgsConstructor
    public class RootTeamCreateStep {

        private final CompanyEntity company;
        private String name;
        private String notes;

        public RootTeamCreateStep name(String name) {
            this.name = name;
            return this;
        }

        public RootTeamCreateStep notes(String notes) {
            this.notes = notes;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public RootTeamStep save() {
            final TeamEntity rootTeam = companyManager.createRootTeam(
                    company.getId(),
                    team ->
                            team
                                    .setName(name)
                                    .setNotes(notes)
            );

            idTestHelper.registerTeam(name, rootTeam.getId());

            return new RootTeamStep(rootTeam);
        }
    }

    @RequiredArgsConstructor
    public final class RootTeamStep implements TeamStep {

        private final TeamEntity rootTeam;

        public TeamEntity get() {
            return rootTeam;
        }

        @SuppressWarnings("UnusedReturnValue")
        public RootTeamStep createSubTeam(Function<SubTeamCreateStep, SubTeamStep> consumer) {
            consumer.apply(new SubTeamCreateStep(rootTeam));
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public RootTeamStep createProject(Function<ProjectTestHelper.ProjectCreateStep, ProjectTestHelper.ProjectStep> consumer) {
            consumer.apply(projectTestHelper.forTeam(get()));

            return this;
        }

        public RootTeamStep addMember(Function<AddMemberStep<RootTeamStep>, MemberStep<RootTeamStep>> consumer) {
            consumer.apply(new AddMemberStep<>(this));

            return this;
        }
    }

    @RequiredArgsConstructor
    public class SubTeamCreateStep {

        private final TeamEntity parentTeam;
        private String name;
        private String notes;

        public SubTeamCreateStep name(String name) {
            this.name = name;
            return this;
        }

        public SubTeamCreateStep notes(String notes) {
            this.notes = notes;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public SubTeamStep save() {
            final TeamEntity subTeam = manager.createSub(
                    parentTeam.getId(),
                    team -> team
                            .setName(name)
                            .setNotes(notes)
            );

            idTestHelper.registerTeam(name, subTeam.getId());

            return new SubTeamStep(subTeam);
        }
    }

    @RequiredArgsConstructor
    public final class SubTeamStep implements TeamStep {

        private final TeamEntity team;

        public TeamEntity get() {
            return team;
        }

        @SuppressWarnings("unused")
        public SubTeamStep createSubTeam(Consumer<SubTeamCreateStep> consumer) {
            consumer.accept(new SubTeamCreateStep(team));
            return this;
        }

        @SuppressWarnings("unused")
        public SubTeamStep createProject(Function<ProjectTestHelper.ProjectCreateStep, ProjectTestHelper.ProjectStep> consumer) {
            consumer.apply(projectTestHelper.forTeam(get()));

            return this;
        }

        public SubTeamStep addMember(Function<AddMemberStep<SubTeamStep>, MemberStep<SubTeamStep>> consumer) {
            consumer.apply(new AddMemberStep<>(this));

            return this;
        }
    }

    @RequiredArgsConstructor
    public class AddMemberStep<P extends TeamStep> {

        private final P parentStep;

        private String firstName;
        private String lastName;
        private String email;
        private String notes;

        private List<TeamMemberRole> roles = new ArrayList<>();

        public AddMemberStep<P> firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AddMemberStep<P> lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AddMemberStep<P> email(String email) {
            this.email = email;
            return this;
        }

        public AddMemberStep<P> notes(String notes) {
            this.notes = notes;
            return this;
        }

        public AddMemberStep<P> roles(TeamMemberRole... roles) {
            this.roles = asList(roles);
            return this;
        }

        public MemberStep<P> save() {
            final TeamMemberEntity member = manager.createAddMember(
                    parentStep.get().getId(),
                    mbr -> mbr
                            .setRoles(roles),
                    p -> p
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setEmail(email)
                            .setNotes(notes)
            );

            idTestHelper.registerPerson(member.getPerson().getName(), member.getPerson().getId());

            return new MemberStep<>(parentStep, member);
        }
    }

    @RequiredArgsConstructor
    @SuppressWarnings("InnerClassMayBeStatic")
    public class MemberStep<P extends TeamStep> {

        private final P parentStep;
        private final TeamMemberEntity memberEntity;

        public P and() {
            return parentStep;
        }

        public TeamMemberEntity get() {
            return memberEntity;
        }
    }

    sealed interface TeamStep permits RootTeamStep, SubTeamStep {

        TeamEntity get();

    }
}
