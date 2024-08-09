package be.sgerard.neo4j.model.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Company Update Request")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class CompanyUpdateRequestDto {

    private final String name;

}
