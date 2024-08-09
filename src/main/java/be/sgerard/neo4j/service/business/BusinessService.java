package be.sgerard.neo4j.service.business;

import be.sgerard.neo4j.model.ServiceProjection;
import be.sgerard.neo4j.model.business.ServiceUsageSearchRequest;

import java.util.List;

public interface BusinessService {

    List<ServiceProjection> findServiceUsages(ServiceUsageSearchRequest request);
}
