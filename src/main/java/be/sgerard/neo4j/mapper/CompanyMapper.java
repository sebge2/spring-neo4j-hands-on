package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.company.CompanyEntity;
import be.sgerard.neo4j.model.dto.company.CompanyCreationRequestDto;
import be.sgerard.neo4j.model.dto.company.CompanyDto;
import be.sgerard.neo4j.model.dto.company.CompanySummaryDto;
import be.sgerard.neo4j.model.dto.company.CompanyUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface CompanyMapper {

    CompanySummaryDto mapToSummaryDto(CompanyEntity company);

    CompanyDto mapToDto(CompanyEntity company);

    void fillFromDto(CompanyCreationRequestDto dto, @MappingTarget CompanyEntity company);

    void fillFromDto(CompanyUpdateRequestDto dto, @MappingTarget CompanyEntity company);
}
