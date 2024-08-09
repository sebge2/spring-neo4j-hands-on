package be.sgerard.neo4j.service.service;

import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ServiceManagerImpl implements ServiceManager {

    private final ServiceRepository repository;

    @Override
    public List<ServiceEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public ServiceEntity create(Consumer<ServiceEntity> initializer) {
        final ServiceEntity project = new ServiceEntity();
        initializer.accept(project);

        return repository.save(project);
    }

    @Override
    public ServiceEntity update(String id, Consumer<ServiceEntity> updater) {
        final ServiceEntity service = findByIdOrDie(id);

        updater.accept(service);

        return repository.save(service);
    }

    @Override
    public Optional<ServiceEntity> deleteById(String id) {
        return findById(id)
                .map(service -> {
                    repository.delete(service);

                    return service;
                });
    }
}
