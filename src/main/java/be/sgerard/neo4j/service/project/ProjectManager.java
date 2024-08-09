package be.sgerard.neo4j.service.project;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ProjectManager {

    List<ProjectEntity> findAll();

    Optional<ProjectEntity> findById(String id);

    default ProjectEntity findByIdOrDie(String id) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no project with id [%s].".formatted(id)));
    }

    ProjectEntity create(Consumer<ProjectEntity> initializer);

    ProjectEntity update(String id, Consumer<ProjectEntity> updater);

    Optional<ProjectEntity> deleteById(String id);

    List<ServiceLinkEntity> findAllLinks(String id);

    Optional<ServiceLinkEntity> findLinkById(String id, String serviceId);

    default ServiceLinkEntity findLinkByIdOrDie(String id, String serviceId) {
        return findLinkById(id, serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no service with id [%s] in project [%s].".formatted(serviceId, id)));
    }

    ServiceLinkEntity createLink(String projectId, String serviceId, Consumer<ServiceLinkEntity> initializer);

    ServiceLinkEntity updateLink(String projectId, String serviceId, Consumer<ServiceLinkEntity> updater);

    Optional<ServiceLinkEntity> deleteLinkById(String projectId, String serviceId);
}
