package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.dto.person.PersonCreationRequestDto;
import be.sgerard.neo4j.model.dto.person.PersonDto;
import be.sgerard.neo4j.model.dto.person.PersonUpdateRequestDto;
import be.sgerard.neo4j.model.person.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto mapToDto(PersonEntity person);

    void fillFromDto(PersonCreationRequestDto dto, @MappingTarget PersonEntity person);

    void fillFromDto(PersonUpdateRequestDto dto, @MappingTarget PersonEntity person);
}
