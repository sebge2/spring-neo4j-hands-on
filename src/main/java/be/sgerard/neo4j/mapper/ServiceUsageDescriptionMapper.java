package be.sgerard.neo4j.mapper;

import be.sgerard.neo4j.model.ServiceProjection;
import be.sgerard.neo4j.model.dto.business.ServiceUsageDescriptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, TeamMapper.class, ProjectMapper.class, ServiceMapper.class, ServiceLinkMapper.class})
public interface ServiceUsageDescriptionMapper {

    ServiceUsageDescriptionDto mapToDto(ServiceProjection projection);

}
