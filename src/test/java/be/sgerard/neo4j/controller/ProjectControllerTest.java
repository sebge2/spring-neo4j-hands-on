package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.dto.project.ProjectUpdateRequestDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkCreationRequestDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkUpdateRequestDto;
import be.sgerard.neo4j.model.service.ServiceType;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("Nothing")
        void nothing() throws Exception {
            project.projects().deleteAll();

            mockMvc.perform(get("/v1/projects"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("One Found")
        void oneFound() throws Exception {
            project.projects().loadByName("Hoo-hoo! App").delete();

            mockMvc.perform(get("/v1/projects"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", equalTo(id.getProjectId("Looney Mobile App"))))
                    .andExpect(jsonPath("$[0].strategy", equalTo("Make it more present in the life of players.")))
                    .andExpect(jsonPath("$[0].objectives", equalTo("Implement objects and people discovery with the camera.")))
                    .andExpect(jsonPath("$[0].difficulties", equalTo("Multi-platforms is a nightmare.")))
                    .andExpect(jsonPath("$[0].notes", equalTo("Main project of the company. A lot of investment on it.")));
        }
    }

    @Nested
    @DisplayName("Find By Id")
    class FindById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(get("/v1/projects/{id}", projectId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(projectId)))
                    .andExpect(jsonPath("$.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$.strategy", equalTo("Make it more present in the life of players.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Implement objects and people discovery with the camera.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Multi-platforms is a nightmare.")))
                    .andExpect(jsonPath("$.notes", equalTo("Main project of the company. A lot of investment on it.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(get("/v1/projects/{id}", 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no project with id [666].")));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(
                            put("/v1/projects/{id}", projectId)
                                    .content(objectMapper.writeValueAsString(
                                            ProjectUpdateRequestDto.builder()
                                                    .name("Looney Mobile App 2")
                                                    .strategy("Make it more present in the life of players. 2")
                                                    .objectives("Implement objects and people discovery with the camera. 2")
                                                    .difficulties("Multi-platforms is a nightmare. 2")
                                                    .notes("Main project of the company. A lot of investment on it. 2")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(projectId)))
                    .andExpect(jsonPath("$.name", equalTo("Looney Mobile App 2")))
                    .andExpect(jsonPath("$.strategy", equalTo("Make it more present in the life of players. 2")))
                    .andExpect(jsonPath("$.objectives", equalTo("Implement objects and people discovery with the camera. 2")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Multi-platforms is a nightmare. 2")))
                    .andExpect(jsonPath("$.notes", equalTo("Main project of the company. A lot of investment on it. 2")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            put("/v1/projects/{id}", 666)
                                    .content(objectMapper.writeValueAsString(
                                            ProjectUpdateRequestDto.builder()
                                                    .name("other")
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
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(delete("/v1/projects/{id}", projectId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(projectId)))
                    .andExpect(jsonPath("$.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$.strategy", equalTo("Make it more present in the life of players.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Implement objects and people discovery with the camera.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Multi-platforms is a nightmare.")))
                    .andExpect(jsonPath("$.notes", equalTo("Main project of the company. A lot of investment on it.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(delete("/v1/projects/{id}", 666))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Find All Links")
    class FindAllLinks {

        @Test
        @DisplayName("Nothing")
        void nothing() throws Exception {
            team.teams()
                    .loadRootByName("IT Department")
                    .createProject(p -> p.name("My project").save());

            final String projectId = id.getProjectId("My project");

            mockMvc.perform(get("/v1/projects/{id}/services", projectId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("One Found")
        void oneFound() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(get("/v1/projects/{id}/services", projectId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[1].state", equalTo(ServiceUsageState.USED.name())))
                    .andExpect(jsonPath("$[1].strategy", equalTo("Share the same backend technology across teams.")))
                    .andExpect(jsonPath("$[1].objectives", equalTo("Replace Scala with Java.")))
                    .andExpect(jsonPath("$[1].difficulties", equalTo("Developers are not trained for Java > 8.")))
                    .andExpect(jsonPath("$[1].notes", equalTo("There is no training sessions to keep them up-to-date.")));
        }
    }

    @Nested
    @DisplayName("Find Link By Id")
    class FindLinkById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(get("/v1/projects/{id}/services/{serviceId}", projectId, serviceId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.service.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$.service.type", equalTo(ServiceType.LANGUAGE.name())))
                    .andExpect(jsonPath("$.state", equalTo(ServiceUsageState.USED.name())))
                    .andExpect(jsonPath("$.strategy", equalTo("Share the same backend technology across teams.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Replace Scala with Java.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Developers are not trained for Java > 8.")))
                    .andExpect(jsonPath("$.notes", equalTo("There is no training sessions to keep them up-to-date.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(get("/v1/projects/{id}/services/{serviceId}", projectId, 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no service with id [666] in project [%s].".formatted(projectId))));
        }
    }

    @Nested
    @DisplayName("Create Link")
    class CreateLink {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(
                            post("/v1/projects/{id}/services?serviceId={serviceId}", projectId, serviceId)
                                    .content(objectMapper.writeValueAsString(
                                            ServiceLinkCreationRequestDto.builder()
                                                    .state(ServiceUsageState.USED)
                                                    .strategy("Share the same backend technology across teams.")
                                                    .objectives("Replace Scala with Java.")
                                                    .difficulties("Developers are not trained for Java > 8.")
                                                    .notes("There is no training sessions to keep them up-to-date.")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.service.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$.service.type", equalTo(ServiceType.LANGUAGE.name())))
                    .andExpect(jsonPath("$.state", equalTo(ServiceUsageState.USED.name())))
                    .andExpect(jsonPath("$.strategy", equalTo("Share the same backend technology across teams.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Replace Scala with Java.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Developers are not trained for Java > 8.")))
                    .andExpect(jsonPath("$.notes", equalTo("There is no training sessions to keep them up-to-date.")));
        }
    }

    @Nested
    @DisplayName("Update Link")
    class UpdateLink {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(
                            put("/v1/projects/{id}/services/{serviceId}", projectId, serviceId)
                                    .content(objectMapper.writeValueAsString(
                                            ServiceLinkUpdateRequestDto.builder()
                                                    .state(ServiceUsageState.ABANDONING)
                                                    .strategy("Share the same backend technology across teams. 2")
                                                    .objectives("Replace Scala with Java. 2")
                                                    .difficulties("Developers are not trained for Java > 8. 2")
                                                    .notes("There is no training sessions to keep them up-to-date. 2")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.service.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$.service.type", equalTo(ServiceType.LANGUAGE.name())))
                    .andExpect(jsonPath("$.state", equalTo(ServiceUsageState.ABANDONING.name())))
                    .andExpect(jsonPath("$.strategy", equalTo("Share the same backend technology across teams. 2")))
                    .andExpect(jsonPath("$.objectives", equalTo("Replace Scala with Java. 2")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Developers are not trained for Java > 8. 2")))
                    .andExpect(jsonPath("$.notes", equalTo("There is no training sessions to keep them up-to-date. 2")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(
                            put("/v1/projects/{id}/services/{serviceId}", projectId, 666)
                                    .content(objectMapper.writeValueAsString(
                                            ServiceLinkUpdateRequestDto.builder()
                                                    .state(ServiceUsageState.ABANDONING)
                                                    .strategy("Share the same backend technology across teams. 2")
                                                    .objectives("Replace Scala with Java. 2")
                                                    .difficulties("Developers are not trained for Java > 8. 2")
                                                    .notes("There is no training sessions to keep them up-to-date. 2")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DeleteLink")
    class DeleteLink {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(delete("/v1/projects/{id}/services/{serviceId}", projectId, serviceId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.service.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$.service.type", equalTo(ServiceType.LANGUAGE.name())))
                    .andExpect(jsonPath("$.state", equalTo(ServiceUsageState.USED.name())))
                    .andExpect(jsonPath("$.strategy", equalTo("Share the same backend technology across teams.")))
                    .andExpect(jsonPath("$.objectives", equalTo("Replace Scala with Java.")))
                    .andExpect(jsonPath("$.difficulties", equalTo("Developers are not trained for Java > 8.")))
                    .andExpect(jsonPath("$.notes", equalTo("There is no training sessions to keep them up-to-date.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            final String projectId = id.getProjectId("Looney Mobile App");

            mockMvc.perform(delete("/v1/projects/{id}/services/{serviceId}", projectId, 666))
                    .andExpect(status().isNoContent());
        }
    }
}