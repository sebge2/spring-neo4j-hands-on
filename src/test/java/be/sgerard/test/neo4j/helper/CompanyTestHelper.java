package be.sgerard.test.neo4j.helper;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import be.sgerard.neo4j.model.team.TeamMemberRole;
import be.sgerard.neo4j.service.company.CompanyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class CompanyTestHelper {

    private final CompanyManager companyManager;
    private final TeamTestHelper teamHelper;
    private final IdTestHelper idTestHelper;

    @SuppressWarnings("UnusedReturnValue")
    public CompanyTestHelper defaultDataSet() {
        companies().create().acmeCorp();

        return this;
    }

    public CompaniesStep companies() {
        return new CompaniesStep();
    }

    public class CompaniesStep {

        public CompanyTestHelper and() {
            return CompanyTestHelper.this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public CompaniesStep assertNumbers(int expected) {
            assertThat(companyManager.search()).hasSize(expected);

            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public CompaniesStep deleteAll() {
            companyManager.search().stream()
                    .map(CompanyEntity::getId)
                    .forEach(companyManager::deleteById);

            return this;
        }

        public CompanyCreateStep create() {
            return new CompanyCreateStep(this);
        }
    }

    @RequiredArgsConstructor
    public class CompanyCreateStep {

        private final CompaniesStep companiesStep;
        private String name;

        public CompanyCreateStep name(String name) {
            this.name = name;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public CompanyStep acmeCorp() {
            final CompanyStep companyStep = companies()
                    .create()
                    .name("ACME")
                    .save();

            companyStep
                    .createRootTeam(rootTeam ->
                            rootTeam.name("IT Department")
                                    .notes("Main department.")
                                    .save()
                                    .createSubTeam(
                                            subTeam -> subTeam
                                                    .name("Looney Team")
                                                    .notes("Team for the Looney application.")
                                                    .save()
                                                    .createProject(
                                                            project -> project
                                                                    .name("Hoo-hoo! App")
                                                                    .strategy("Make more people adopt it.")
                                                                    .objectives("Implement more aggressive features in the mobile app.")
                                                                    .difficulties(null)
                                                                    .notes("Side project of the company. Investment limited.")
                                                                    .save()
                                                                    .createLinkWithService(
                                                                            "Java",
                                                                            link -> link
                                                                                    .state(ServiceUsageState.USED)
                                                                                    .strategy("Migrate from Cloud functions to Java.")
                                                                                    .objectives("Migrate by the end of 2024.")
                                                                                    .difficulties(null)
                                                                                    .notes("Small backend, more frontend app.")
                                                                                    .save()
                                                                    )
                                                    )
                                                    .addMember(
                                                            member -> member
                                                                    .firstName("Daffy")
                                                                    .lastName("Duck")
                                                                    .email("daffy.duck@acme.com")
                                                                    .notes("Developer.")
                                                                    .roles(TeamMemberRole.DEVELOPER)
                                                                    .save()
                                                    )
                                    )
                                    .createProject(
                                            project -> project
                                                    .name("Looney Mobile App")
                                                    .strategy("Make it more present in the life of players.")
                                                    .objectives("Implement objects and people discovery with the camera.")
                                                    .difficulties("Multi-platforms is a nightmare.")
                                                    .notes("Main project of the company. A lot of investment on it.")
                                                    .save()
                                                    .createLinkWithService(
                                                            "Java",
                                                            link -> link
                                                                    .state(ServiceUsageState.USED)
                                                                    .strategy("Share the same backend technology across teams.")
                                                                    .objectives("Replace Scala with Java.")
                                                                    .difficulties("Developers are not trained for Java > 8.")
                                                                    .notes("There is no training sessions to keep them up-to-date.")
                                                                    .save()
                                                    )
                                                    .createLinkWithService(
                                                            "Angular",
                                                            link -> link
                                                                    .state(ServiceUsageState.ABANDONING)
                                                                    .strategy(null)
                                                                    .objectives(null)
                                                                    .difficulties(null)
                                                                    .notes(null)
                                                                    .save()
                                                    )
                                    )
                                    .addMember(
                                            member -> member
                                                    .firstName("Bugs")
                                                    .lastName("Bunny")
                                                    .email("bugs.bunny@acme.com")
                                                    .notes("Director.")
                                                    .roles(TeamMemberRole.LEADER)
                                                    .save()
                                    )
                    );

            return companyStep;
        }

        public CompanyStep save() {
            final CompanyEntity company = companyManager.create(c -> c.setName(name));

            idTestHelper.registerCompany(name, company.getId());

            return new CompanyStep(companiesStep, company);
        }
    }

    @RequiredArgsConstructor
    public class CompanyStep {

        private final CompaniesStep companiesStep;
        private final CompanyEntity company;

        public CompaniesStep and() {
            return companiesStep;
        }

        public CompanyEntity get() {
            return company;
        }

        public String getId() {
            return get().getId();
        }

        @SuppressWarnings("UnusedReturnValue")
        public CompanyStep createRootTeam(Function<TeamTestHelper.RootTeamCreateStep, TeamTestHelper.RootTeamStep> consumer) {
            consumer.apply(teamHelper.forRoot(get()));

            return this;
        }
    }
}
