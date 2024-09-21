package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.project.ServiceLinkCreationRequestDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkDto;
import be.sgerard.neo4j.model.dto.project.ServiceLinkUpdateRequestDto;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ServiceLinkMapper {

    ServiceLinkDto mapToDto(ServiceLinkEntity link);

    void fillFromDto(ServiceLinkCreationRequestDto dto, @MappingTarget ServiceLinkEntity link);

    void fillFromDto(ServiceLinkUpdateRequestDto dto, @MappingTarget ServiceLinkEntity link);
}
