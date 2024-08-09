package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.dto.company.CompanyCreationRequestDto;
import be.sgerard.neo4j.model.dto.company.CompanyUpdateRequestDto;
import be.sgerard.neo4j.model.dto.team.RootTeamCreationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CompanyControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("No Company")
        void noCompany() throws Exception {
            data.deleteAll();

            mockMvc.perform(get("/v1/companies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("One Company")
        void oneCompany() throws Exception {
            final String companyId = id.getCompanyId("ACME");

            mockMvc.perform(get("/v1/companies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", equalTo(companyId)))
                    .andExpect(jsonPath("$[0].name", equalTo("ACME")));
        }
    }

    @Nested
    @DisplayName("Find By Id")
    class FindById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String companyId = id.getCompanyId("ACME");

            mockMvc.perform(get("/v1/companies/{id}", companyId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(companyId)))
                    .andExpect(jsonPath("$.name", equalTo("ACME")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(get("/v1/companies/{id}", 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no company with id [666].")));
        }
    }

    @Nested
    @DisplayName("Create")
    class Create {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            mockMvc.perform(
                            post("/v1/companies")
                                    .content(objectMapper.writeValueAsString(
                                            CompanyCreationRequestDto.builder()
                                                    .name("other")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name", equalTo("other")));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String companyId = id.getCompanyId("ACME");

            mockMvc.perform(
                            put("/v1/companies/{id}", companyId)
                                    .content(objectMapper.writeValueAsString(
                                            CompanyUpdateRequestDto.builder()
                                                    .name("other")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(companyId)))
                    .andExpect(jsonPath("$.name", equalTo("other")));
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
            final String companyId = id.getCompanyId("ACME");

            mockMvc.perform(delete("/v1/companies/{id}", companyId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(companyId)))
                    .andExpect(jsonPath("$.name", equalTo("ACME")));

            company.companies().assertNumbers(0);
            project.projects().assertNumbers(0);
            team.teams().assertNumbers(0);
            person.persons().assertNumbers(0);
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(delete("/v1/companies/{id}", 666))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Create Root Team")
    class CreateRootTeam {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String companyId = id.getCompanyId("ACME");

            mockMvc.perform(
                            post("/v1/companies/{id}/root-team", companyId)
                                    .content(objectMapper.writeValueAsString(
                                            RootTeamCreationRequestDto.builder()
                                                    .name("my team")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.name", equalTo("my team")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            post("/v1/companies/{id}/root-team", 666)
                                    .content(objectMapper.writeValueAsString(
                                            RootTeamCreationRequestDto.builder()
                                                    .name("my team")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound());
        }
    }
}