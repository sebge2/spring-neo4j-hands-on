package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.model.dto.person.PersonUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PersonControllerTest extends AbstractControllerTest {

    @Nested
    @DisplayName("Find All")
    class FindAll {

        @Test
        @DisplayName("No person")
        void noPerson() throws Exception {
            person.persons().deleteAll();

            mockMvc.perform(get("/v1/persons"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Person Found")
        void personFound() throws Exception {
            person.persons().loadByName("Daffy Duck").delete();

            mockMvc.perform(get("/v1/persons"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", equalTo(id.getPersonId("Bugs Bunny"))))
                    .andExpect(jsonPath("$[0].firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$[0].lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$[0].email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$[0].notes", equalTo("Director.")));
        }
    }

    @Nested
    @DisplayName("Find By Id")
    class FindById {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(get("/v1/persons/{id}", personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(personId)))
                    .andExpect(jsonPath("$.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$.notes", equalTo("Director.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(get("/v1/persons/{id}", 666))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", equalTo("There is no person with id [666].")));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {

        @Test
        @DisplayName("Found")
        void found() throws Exception {
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(
                            put("/v1/persons/{id}", personId)
                                    .content(objectMapper.writeValueAsString(
                                            PersonUpdateRequestDto.builder()
                                                    .firstName("Bugsy")
                                                    .lastName("Bunnyy")
                                                    .email("bugsy.bunnyy@acme.com")
                                                    .notes("Director!")
                                                    .build()
                                    ))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(personId)))
                    .andExpect(jsonPath("$.firstName", equalTo("Bugsy")))
                    .andExpect(jsonPath("$.lastName", equalTo("Bunnyy")))
                    .andExpect(jsonPath("$.email", equalTo("bugsy.bunnyy@acme.com")))
                    .andExpect(jsonPath("$.notes", equalTo("Director!")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(
                            put("/v1/persons/{id}", 666)
                                    .content(objectMapper.writeValueAsString(
                                            PersonUpdateRequestDto.builder()
                                                    .firstName("other")
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
            final String personId = id.getPersonId("Bugs Bunny");

            mockMvc.perform(delete("/v1/persons/{id}", personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(personId)))
                    .andExpect(jsonPath("$.firstName", equalTo("Bugs")))
                    .andExpect(jsonPath("$.lastName", equalTo("Bunny")))
                    .andExpect(jsonPath("$.email", equalTo("bugs.bunny@acme.com")))
                    .andExpect(jsonPath("$.notes", equalTo("Director.")));
        }

        @Test
        @DisplayName("Not Found")
        void notFound() throws Exception {
            mockMvc.perform(delete("/v1/persons/{id}", 666))
                    .andExpect(status().isNoContent());
        }
    }
}
