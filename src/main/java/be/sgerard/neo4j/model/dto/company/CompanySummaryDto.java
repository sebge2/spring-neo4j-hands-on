package be.sgerard.neo4j.model.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Company Summary")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class CompanySummaryDto {

    private final String id;
    private final String name;

}
