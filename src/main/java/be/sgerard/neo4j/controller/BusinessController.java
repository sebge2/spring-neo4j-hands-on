package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.ServiceUsageDescriptionMapper;
import be.sgerard.neo4j.model.business.ServiceUsageSearchRequest;
import be.sgerard.neo4j.model.dto.business.ServiceUsageDescriptionDto;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import be.sgerard.neo4j.service.business.BusinessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@Tag(name = "Business")
@RestController
@RequestMapping("/v1/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService service;
    private final ServiceUsageDescriptionMapper mapper;

    @GetMapping("/services")
    public List<ServiceUsageDescriptionDto> findServices(@RequestParam(name = "serviceId", required = false) Collection<String> serviceIds,
                                                         @RequestParam(name = "serviceStates", required = false) Collection<ServiceUsageState> states,
                                                         @RequestParam(required = false) boolean onlyWithDifficulties,
                                                         @RequestParam(required = false) boolean onlyWithStrategies) {
        return service
                .findServiceUsages(
                        new ServiceUsageSearchRequest(
                                serviceIds,
                                states,
                                onlyWithDifficulties,
                                onlyWithStrategies
                        )
                )
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

}
