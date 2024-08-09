package be.sgerard.neo4j.service.project;

import be.sgerard.neo4j.ResourceNotFoundException;
import be.sgerard.neo4j.model.project.ProjectEntity;
import be.sgerard.neo4j.model.service.ServiceEntity;
import be.sgerard.neo4j.model.service.ServiceLinkEntity;
import be.sgerard.neo4j.repository.ProjectRepository;
import be.sgerard.neo4j.service.service.ServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ProjectManagerImpl implements ProjectManager {

    private final ProjectRepository projectRepository;
    private final ServiceManager serviceManager;

    @Override
    public List<ProjectEntity> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<ProjectEntity> findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public ProjectEntity create(Consumer<ProjectEntity> initializer) {
        final ProjectEntity project = new ProjectEntity();
        initializer.accept(project);

        return projectRepository.save(project);
    }

    @Override
    public ProjectEntity update(String id, Consumer<ProjectEntity> updater) {
        final ProjectEntity project = findByIdOrDie(id);

        updater.accept(project);

        return projectRepository.save(project);
    }

    @Override
    public Optional<ProjectEntity> deleteById(String id) {
        return findById(id)
                .map(project -> {
                    projectRepository.delete(project);

                    return project;
                });
    }

    @Override
    public List<ServiceLinkEntity> findAllLinks(String id) {
        return findByIdOrDie(id).getServices().stream()
                .sorted(Comparator.comparing(l-> l.getService().getName()))
                .toList();
    }

    @Override
    public Optional<ServiceLinkEntity> findLinkById(String id, String serviceId) {
        final ProjectEntity project = findByIdOrDie(id);

        return project.getServices().stream()
                .filter(currentLink -> Objects.equals(currentLink.getService().getId(), serviceId))
                .findFirst();
    }

    @Override
    public ServiceLinkEntity createLink(String projectId, String serviceId, Consumer<ServiceLinkEntity> initializer) {
        final ProjectEntity project = findByIdOrDie(projectId);
        final ServiceEntity service = serviceManager.findByIdOrDie(serviceId);

        final ServiceLinkEntity link = new ServiceLinkEntity();
        initializer.accept(link);
        link.setService(service);

        project.getServices().add(link);

        projectRepository.save(project);

        return link;
    }

    @Override
    public ServiceLinkEntity updateLink(String projectId, String serviceId, Consumer<ServiceLinkEntity> updater) {
        final ProjectEntity project = findByIdOrDie(projectId);

        return project.getServices().stream()
                .filter(currentLink -> Objects.equals(currentLink.getService().getId(), serviceId))
                .findFirst()
                .map(link -> {
                    updater.accept(link);

                    projectRepository.save(project);

                    return link;
                })
                .orElseThrow(() -> new ResourceNotFoundException("There is no link for service [%s] in project [%s].".formatted(serviceId, projectId)));
    }

    @Override
    public Optional<ServiceLinkEntity> deleteLinkById(String projectId, String serviceId) {
        final ProjectEntity project = findByIdOrDie(projectId);

        return project.getServices().stream()
                .filter(currentLink -> Objects.equals(currentLink.getService().getId(), serviceId))
                .findFirst()
                .map(link -> {
                    project.getServices().remove(link);

                    projectRepository.save(project);

                    return link;
                });
    }
}
