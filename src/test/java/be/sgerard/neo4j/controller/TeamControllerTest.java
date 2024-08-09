package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.dto.person.PersonCreationRequestDto;
import be.sgerard.neo4j.model.dto.project.ProjectCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.PersonCreationMemberRequestDto;
import be.sgerard.neo4j.model.dto.team.SubTeamCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamMemberUpdateRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamUpdateRequestDto;
import be.sgerard.neo4j.model.team.TeamMemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("No Team")
        void noTeam() throws Exception {
            data.deleteAll();

            mockMvc.perform(get("/v1/teams"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Team Found")
        void teamFound() throws Exception {
            mockMvc.perform(get("/v1/teams"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", equalTo(id.getTeamId("IT Department"))))
                    .andExpect(jsonPath("$[0].name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[1].id", equalTo(id.getTeamId("Looney Team"))))
                    .andExpect(jsonPath("$[1].name", equalTo("Looney Team")));
        }
    }

    @Nested
    @DisplayName("Find By Id")
    class FindById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(teamId)))
                    .andExpect(jsonPath("$.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$.notes", equalTo("Main department.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(get("/v1/teams/{id}", 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no team with id [666].")));
        }
    }

    @Nested
    @DisplayName("Find All Sub Teams")
    class FindAllSubTeams {

        @Test
        @DisplayName("Nothing")
        void nothing() throws Exception {
            person.persons().deleteAll();

            final String teamId = id.getTeamId("Looney Team");

            mockMvc.perform(get("/v1/teams/{id}/sub-teams", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}/sub-teams", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").isNotEmpty())
                    .andExpect(jsonPath("$[0].name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$[0].notes", equalTo("Team for the Looney application.")));
        }
    }

    @Nested
    @DisplayName("Create Sub-Team")
    class CreateSubTeam {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(
                            post("/v1/teams/{id}/sub-teams", teamId)
                                    .content(objectMapper.writeValueAsString(
                                            SubTeamCreationRequestDto.builder()
                                                    .name("My Sub Team")
                                                    .notes("My notes.")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name", equalTo("My Sub Team")))
                    .andExpect(jsonPath("$.notes", equalTo("My notes.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            post("/v1/teams/{id}/sub-teams", 666)
                                    .content(objectMapper.writeValueAsString(
                                            SubTeamCreationRequestDto.builder()
                                                    .name("My Sub Team")
                                                    .notes("My notes.")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no team with id [666].")));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(
                            put("/v1/teams/{id}", teamId)
                                    .content(objectMapper.writeValueAsString(
                                            TeamUpdateRequestDto.builder()
                                                    .name("other")
                                                    .notes("my notes")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(teamId)))
                    .andExpect(jsonPath("$.name", equalTo("other")))
                    .andExpect(jsonPath("$.notes", equalTo("my notes")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            put("/v1/teams/{id}", 666)
                                    .content(objectMapper.writeValueAsString(
                                            TeamUpdateRequestDto.builder()
                                                    .name("other")
                                                    .notes("my notes")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Delete")
    class Delete {

        @Test
        @DisplayName("Found Root")
        void foundRoot() throws Exception {
            project.projects().assertNumbers(2);
            person.persons().assertNumbers(2);
            team.teams().assertNumbers(2);

            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(delete("/v1/teams/{id}", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(teamId)))
                    .andExpect(jsonPath("$.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$.notes", equalTo("Main department.")));

            project.projects().assertNumbers(0);
            person.persons().assertNumbers(0);
            team.teams().assertNumbers(0);
        }

        @Test
        @DisplayName("Found Sub")
        void foundSub() throws Exception {
            project.projects().assertNumbers(2);
            person.persons().assertNumbers(2);
            team.teams().assertNumbers(2);

            final String teamId = id.getTeamId("Looney Team");

            mockMvc.perform(delete("/v1/teams/{id}", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(teamId)))
                    .andExpect(jsonPath("$.name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$.notes", equalTo("Team for the Looney application.")));

            project.projects().assertNumbers(1);
            person.persons().assertNumbers(1);
            team.teams().assertNumbers(1);
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(delete("/v1/teams/{id}", 666))
                    .andExpect(status().isNoContent());
        }
    }

    // TODO create add member
    // TODO find member
    // TODO add member
    // TODO delete member
    // TODO create project

    @Nested
    @DisplayName("Find All Members")
    class FindAllMembers {

        @Test
        @DisplayName("Nothing")
        void nothing() throws Exception {
            person.persons().deleteAll();

            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}/members", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Team Found")
        void teamFound() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}/members", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].roles", equalTo(singletonList(TeamMemberRole.LEADER.name()))))
                    .andExpect(jsonPath("$[0].person.id", equalTo(id.getPersonId("Bugs Bunny"))))
                    .andExpect(jsonPath("$[0].person.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$[0].person.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$[0].person.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$[0].person.notes", equalTo("Director.")));
        }
    }

    @Nested
    @DisplayName("CreateAdd Member")
    class CreateAddMember {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(
                            post("/v1/teams/{id}/members", teamId)
                                    .content(objectMapper.writeValueAsString(
                                            PersonCreationMemberRequestDto.builder()
                                                    .person(
                                                            PersonCreationRequestDto.builder()
                                                                    .firstName("Bugs")
                                                                    .lastName("Bunny")
                                                                    .email("bugs.bunny@acme.com")
                                                                    .notes("Director.")
                                                                    .build()
                                                    )
                                                    .roles(singletonList(TeamMemberRole.DEVELOPER))
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roles", equalTo(singletonList(TeamMemberRole.DEVELOPER.name()))))
                    .andExpect(jsonPath("$.person.id").isNotEmpty())
                    .andExpect(jsonPath("$.person.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$.person.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$.person.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$.person.notes", equalTo("Director.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            post("/v1/teams/{id}/members", 666)
                                    .content(objectMapper.writeValueAsString(
                                            PersonCreationMemberRequestDto.builder()
                                                    .person(
                                                            PersonCreationRequestDto.builder()
                                                                    .firstName("Bugs")
                                                                    .lastName("Bunny")
                                                                    .email("bugs.bunny@acme.com")
                                                                    .notes("Director.")
                                                                    .build()
                                                    )
                                                    .roles(singletonList(TeamMemberRole.DEVELOPER))
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no team with id [666].")));
        }
    }

    @Nested
    @DisplayName("Update Member")
    class UpdateMember {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(
                            put("/v1/teams/{id}/members/{personId}", teamId, personId)
                                    .content(objectMapper.writeValueAsString(
                                            TeamMemberUpdateRequestDto.builder()
                                                    .roles(singletonList(TeamMemberRole.ANALYST))
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roles", equalTo(singletonList(TeamMemberRole.ANALYST.name()))))
                    .andExpect(jsonPath("$.person.id", equalTo(id.getPersonId("Bugs Bunny"))))
                    .andExpect(jsonPath("$.person.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$.person.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$.person.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$.person.notes", equalTo("Director.")));
        }

        @Test
        @DisplayName("Team Not Found")
        void teamNotFound() throws Exception {
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(
                            put("/v1/teams/{id}/{personId}", 666, personId)
                                    .content(objectMapper.writeValueAsString(
                                            TeamUpdateRequestDto.builder()
                                                    .name("other")
                                                    .notes("my notes")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Person Not Found")
        void personNotFound() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(
                            put("/v1/teams/{id}/{personId}", teamId, 666)
                                    .content(objectMapper.writeValueAsString(
                                            TeamUpdateRequestDto.builder()
                                                    .name("other")
                                                    .notes("my notes")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Delete Member")
    class DeleteMember {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(delete("/v1/teams/{id}/members/{personId}", teamId, personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roles", equalTo(singletonList(TeamMemberRole.LEADER.name()))))
                    .andExpect(jsonPath("$.person.id", equalTo(id.getPersonId("Bugs Bunny"))))
                    .andExpect(jsonPath("$.person.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$.person.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$.person.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$.person.notes", equalTo("Director.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(delete("/v1/teams/{id}/members/{personId}", teamId, 666))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Find All Projects")
    class FindAllProjects {

        @Test
        @DisplayName("Nothing")
        void nothing() throws Exception {
            project.projects().deleteAll();

            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}/projects", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(get("/v1/teams/{id}/projects", teamId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[0].strategy", equalTo("Make it more present in the life of players.")))
                    .andExpect(jsonPath("$[0].objectives", equalTo("Implement objects and people discovery with the camera.")))
                    .andExpect(jsonPath("$[0].difficulties", equalTo("Multi-platforms is a nightmare.")))
                    .andExpect(jsonPath("$[0].notes", equalTo("Main project of the company. A lot of investment on it.")));
        }
    }

    @Nested
    @DisplayName("Create Project")
    class CreateProject {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String teamId = id.getTeamId("IT Department");

            mockMvc.perform(
                            post("/v1/teams/{id}/projects", teamId)
                                    .content(objectMapper.writeValueAsString(
                                            ProjectCreationRequestDto.builder()
                                                    .name("Looney Mobile App")
                                                    .strategy("Make it more present in the life of players.")
                                                    .objectives("Implement objects and people discovery with the camera.")
                                                    .difficulties("Multi-platforms is a nightmare.")
                                                    .notes("Main project of the company. A lot of investment on it.")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$.strategy", equalTo("Make it more present in the life of players.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Implement objects and people discovery with the camera.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Multi-platforms is a nightmare.")))
                    .andExpect(jsonPath("$.notes", equalTo("Main project of the company. A lot of investment on it.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            post("/v1/teams/{id}/projects", 666)
                                    .content(objectMapper.writeValueAsString(
                                            ProjectCreationRequestDto.builder()
                                                    .name("Looney Mobile App")
                                                    .strategy("Make it more present in the life of players.")
                                                    .objectives("Implement objects and people discovery with the camera.")
                                                    .difficulties("Multi-platforms is a nightmare.")
                                                    .notes("Main project of the company. A lot of investment on it.")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no team with id [666].")));
        }
    }
}