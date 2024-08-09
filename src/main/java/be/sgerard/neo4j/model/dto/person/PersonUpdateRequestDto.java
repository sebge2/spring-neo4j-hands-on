package be.sgerard.neo4j.model.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Person Update Request")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class PersonUpdateRequestDto {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String notes;

}
