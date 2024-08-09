package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.ProjectMapper;
import be.sgerard.neo4j.mapper.ServiceLinkMapper;
import be.sgerard.neo4j.model.dto.project.*;
import be.sgerard.neo4j.service.project.ProjectManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project")
@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectManager manager;
    private final ProjectMapper projectMapper;
    private final ServiceLinkMapper serviceLinkMapper;

    @GetMapping
    List<ProjectSummaryDto> findAll() {
        return manager.findAll().stream()
                .map(projectMapper::mapToSummaryDto)
                .toList();
    }

    @GetMapping("/{id}")
    ProjectSummaryDto findById(@PathVariable String id) {
        return projectMapper.mapToSummaryDto(
                manager.findByIdOrDie(id)
        );
    }

    @PutMapping(value = "/{id}")
    ProjectSummaryDto update(@PathVariable String id,
                             @RequestBody ProjectUpdateRequestDto dto) {
        return projectMapper.mapToSummaryDto(
                manager.update(id, project -> projectMapper.fillFromDto(dto, project))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<ProjectSummaryDto> delete(@PathVariable String id) {
        return manager.deleteById(id)
                .map(projectMapper::mapToSummaryDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/{id}/services")
    List<ServiceLinkDto> findAllLinks(@PathVariable(name = "id") String id) {
        return manager.findAllLinks(id).stream()
                .map(serviceLinkMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}/services/{serviceId}")
    ServiceLinkDto findLinkById(@PathVariable String id,
                                @PathVariable(name = "serviceId") String serviceId) {
        return serviceLinkMapper.mapToDto(
                manager.findLinkByIdOrDie(id, serviceId)
        );
    }

    @PostMapping("/{id}/services")
    ServiceLinkDto createLink(@PathVariable(name = "id") String projectId,
                              @RequestParam(name = "serviceId") String serviceId,
                              @RequestBody ServiceLinkCreationRequestDto dto) {
        return serviceLinkMapper.mapToDto(
                manager.createLink(
                        projectId,
                        serviceId,
                        link -> serviceLinkMapper.fillFromDto(dto, link)
                )
        );
    }

    @PutMapping("/{id}/services/{serviceId}")
    ServiceLinkDto updateLink(@PathVariable(name = "id") String projectId,
                              @PathVariable(name = "serviceId") String serviceId,
                              @RequestBody ServiceLinkUpdateRequestDto dto) {
        return serviceLinkMapper.mapToDto(
                manager.updateLink(
                        projectId,
                        serviceId,
                        link -> serviceLinkMapper.fillFromDto(dto, link)
                )
        );
    }

    @DeleteMapping("/{id}/services/{serviceId}")
    ResponseEntity<ServiceLinkDto> deleteLink(@PathVariable(name = "id") String projectId,
                                              @PathVariable(name = "serviceId") String serviceId) {
        return manager.deleteLinkById(projectId, serviceId)
                .map(serviceLinkMapper::mapToDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
