package be.sgerard.neo4j.model.business;

import be.sgerard.neo4j.model.service.ServiceUsageState;

import java.util.Collection;

public record ServiceUsageSearchRequest(Collection<String> serviceIds,
                                        Collection<ServiceUsageState> states,
                                        boolean onlyWithDifficulties,
                                        boolean onlyWithStrategies) {

}
