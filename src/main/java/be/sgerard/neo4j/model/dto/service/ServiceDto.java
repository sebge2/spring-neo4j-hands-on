package be.sgerard.neo4j.model.dto.service;

import be.sgerard.neo4j.model.service.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "Service")
@Builder(toBuilder = true)
@Jacksonized
@Getter
@Setter
public class ServiceDto {

    private final String id;
    private final String name;
    private final ServiceType type;
}
