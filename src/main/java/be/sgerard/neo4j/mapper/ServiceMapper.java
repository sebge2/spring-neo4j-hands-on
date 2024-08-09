package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.service.ServiceCreationRequestDto;
import be.sgerard.neo4j.model.dto.service.ServiceDto;
import be.sgerard.neo4j.model.dto.service.ServiceUpdateRequestDto;
import be.sgerard.neo4j.model.service.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDto mapToDto(ServiceEntity service);

    void fillFromDto(ServiceCreationRequestDto dto, @MappingTarget ServiceEntity service);

    void fillFromDto(ServiceUpdateRequestDto dto, @MappingTarget ServiceEntity service);

}
