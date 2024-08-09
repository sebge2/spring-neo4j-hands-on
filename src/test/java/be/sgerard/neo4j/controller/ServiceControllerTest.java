package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.dto.company.CompanyUpdateRequestDto;
import be.sgerard.neo4j.model.dto.service.ServiceCreationRequestDto;
import be.sgerard.neo4j.model.dto.service.ServiceUpdateRequestDto;
import be.sgerard.neo4j.model.service.ServiceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ServiceControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("No Service")
        void noService() throws Exception {
            service.services().deleteAll();

            mockMvc.perform(get("/v1/services"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Service Found")
        void serviceFound() throws Exception {
            service.services().deleteAll()
                    .create().java();

            mockMvc.perform(get("/v1/services"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", equalTo(id.getServiceId("Java"))))
                    .andExpect(jsonPath("$[0].name", equalTo("Java")))
                    .andExpect(jsonPath("$[0].type", equalTo(ServiceType.LANGUAGE.name())));
        }
    }

    @Nested
    @DisplayName("Find By Id")
    class FindById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(get("/v1/services/{id}", serviceId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.name", equalTo("Java")))
                    .andExpect(jsonPath("$.type", equalTo(ServiceType.LANGUAGE.name())));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(get("/v1/services/{id}", 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no service with id [666].")));
        }
    }

    @Nested
    @DisplayName("Create")
    class Create {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            mockMvc.perform(
                            post("/v1/services")
                                    .content(objectMapper.writeValueAsString(
                                            ServiceCreationRequestDto.builder()
                                                    .name("other")
                                                    .type(ServiceType.OTHER)
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name", equalTo("other")))
                    .andExpect(jsonPath("$.type", equalTo(ServiceType.OTHER.name())));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(
                            put("/v1/services/{id}", serviceId)
                                    .content(objectMapper.writeValueAsString(
                                            ServiceUpdateRequestDto.builder()
                                                    .name("other")
                                                    .type(ServiceType.OTHER)
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.name", equalTo("other")))
                    .andExpect(jsonPath("$.type", equalTo(ServiceType.OTHER.name())));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            put("/v1/companies/{id}", 666)
                                    .content(objectMapper.writeValueAsString(
                                            CompanyUpdateRequestDto.builder()
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
            final String serviceId = id.getServiceId("Java");

            mockMvc.perform(delete("/v1/services/{id}", serviceId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(serviceId)))
                    .andExpect(jsonPath("$.name", equalTo("Java")))
                    .andExpect(jsonPath("$.type", equalTo(ServiceType.LANGUAGE.name())));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(delete("/v1/services/{id}", 666))
                    .andExpect(status().isNoContent());
        }
    }
}