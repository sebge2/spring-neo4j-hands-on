package be.sgerard.neo4j.service.business;

import be.sgerard.neo4j.model.ServiceProjection;
import be.sgerard.neo4j.model.business.ServiceUsageSearchRequest;
import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceUsageState;
import be.sgerard.neo4j.repository.ServiceRepository;
import be.sgerard.neo4j.service.service.ServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final ServiceRepository repository;
    private final ServiceManager serviceManager;

    @Override
    public List<ServiceProjection> findServiceUsages(ServiceUsageSearchRequest request) {
        final ServiceUsageSearchRequest completedRequest = completeRequest(request);

        return repository.findServices(completedRequest.serviceIds(), completedRequest.states(), completedRequest.onlyWithDifficulties(), completedRequest.onlyWithStrategies()).stream()
                .peek(proj -> proj.getServiceLink().setService(proj.getService()))
                .toList();
    }

    private ServiceUsageSearchRequest completeRequest(ServiceUsageSearchRequest request) {
        return new ServiceUsageSearchRequest(
                completeServiceIds(request.serviceIds()),
                completeUsageStates(request.states()),
                request.onlyWithDifficulties(),
                request.onlyWithStrategies()
        );
    }

    private Collection<String> completeServiceIds(Collection<String> serviceIds) {
        return Optional.ofNullable(serviceIds)
                .filter(ids -> !ids.isEmpty())
                .orElseGet(() -> serviceManager.findAll().stream().map(ServiceEntity::getId).toList());
    }

    private Collection<ServiceUsageState> completeUsageStates(Collection<ServiceUsageState> states) {
        return Optional.ofNullable(states)
                .filter(ids -> !ids.isEmpty())
                .orElseGet(() -> Stream.of(ServiceUsageState.values()).toList());
    }
}
