package be.sgerard.neo4j.model.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Person")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class PersonDto {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String notes;

}
