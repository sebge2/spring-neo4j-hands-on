package be.sgerard.neo4j.service.service;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.service.ServiceEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ServiceManager {

    List<ServiceEntity> findAll();

    Optional<ServiceEntity> findById(String id);

    default ServiceEntity findByIdOrDie(String id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no service with id [%s].".formatted(id)));
    }

    ServiceEntity create(Consumer<ServiceEntity> initializer);

    ServiceEntity update(String id, Consumer<ServiceEntity> updater);

    Optional<ServiceEntity> deleteById(String id);

}
