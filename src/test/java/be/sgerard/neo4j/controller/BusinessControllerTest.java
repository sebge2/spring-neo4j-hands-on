package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.service.ServiceUsageState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BusinessControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find Services")
    class FindServices {

        @Test
        @DisplayName("Find All Usages")
        void findAllUsages() throws Exception {
            mockMvc.perform(
                            get("/v1/business/services")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))

                    .andExpect(jsonPath("$[0].service.name", equalTo("Angular")))
                    .andExpect(jsonPath("$[0].serviceLink.service.name", equalTo("Angular")))
                    .andExpect(jsonPath("$[0].serviceLink.state", equalTo("ABANDONING")))
                    .andExpect(jsonPath("$[0].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[0].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[0].company.name", equalTo("ACME")))

                    .andExpect(jsonPath("$[1].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[1].project.name", equalTo("Hoo-hoo! App")))
                    .andExpect(jsonPath("$[1].team.name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$[1].company.name", equalTo("ACME")))

                    .andExpect(jsonPath("$[2].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[2].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[2].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[2].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[2].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[2].company.name", equalTo("ACME")));
        }

        @Test
        @DisplayName("Find By Service")
        void findByService() throws Exception {
            final String java = id.getServiceId("Java");

            mockMvc.perform(
                            get("/v1/business/services?serviceId={serviceId}", java)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))

                    .andExpect(jsonPath("$[0].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[0].project.name", equalTo("Hoo-hoo! App")))
                    .andExpect(jsonPath("$[0].team.name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$[0].company.name", equalTo("ACME")))

                    .andExpect(jsonPath("$[1].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[1].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[1].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[1].company.name", equalTo("ACME")));
        }

        @Test
        @DisplayName("Find Only Difficulties")
        void findOnlyDifficulties() throws Exception {
            mockMvc.perform(
                            get("/v1/business/services?onlyWithDifficulties=true")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))

                    .andExpect(jsonPath("$[0].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[0].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[0].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[0].company.name", equalTo("ACME")));
        }

        @Test
        @DisplayName("Find Only Strategies")
        void findOnlyStrategies() throws Exception {
            mockMvc.perform(
                            get("/v1/business/services?onlyWithStrategies=true")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))

                    .andExpect(jsonPath("$[0].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[0].project.name", equalTo("Hoo-hoo! App")))
                    .andExpect(jsonPath("$[0].team.name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$[0].company.name", equalTo("ACME")))

                    .andExpect(jsonPath("$[1].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[1].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[1].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[1].company.name", equalTo("ACME")));
        }

        @Test
        @DisplayName("Find by State")
        void findByState() throws Exception {
            mockMvc.perform(
                            get("/v1/business/services?serviceStates={state}", ServiceUsageState.USED)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))

                    .andExpect(jsonPath("$[0].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[0].project.name", equalTo("Hoo-hoo! App")))
                    .andExpect(jsonPath("$[0].team.name", equalTo("Looney Team")))
                    .andExpect(jsonPath("$[0].company.name", equalTo("ACME")))

                    .andExpect(jsonPath("$[1].service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.service.name", equalTo("Java")))
                    .andExpect(jsonPath("$[1].serviceLink.state", equalTo("USED")))
                    .andExpect(jsonPath("$[1].project.name", equalTo("Looney Mobile App")))
                    .andExpect(jsonPath("$[1].team.name", equalTo("IT Department")))
                    .andExpect(jsonPath("$[1].company.name", equalTo("ACME")));
        }
    }
}