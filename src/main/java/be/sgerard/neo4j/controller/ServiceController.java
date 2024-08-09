package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.ServiceMapper;
import be.sgerard.neo4j.model.dto.service.ServiceCreationRequestDto;
import be.sgerard.neo4j.model.dto.service.ServiceDto;
import be.sgerard.neo4j.model.dto.service.ServiceUpdateRequestDto;
import be.sgerard.neo4j.service.service.ServiceManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Service")
@RestController
@RequestMapping("/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceManager manager;
    private final ServiceMapper mapper;

    @GetMapping
    List<ServiceDto> findAll() {
        return manager.findAll().stream()
                .map(mapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    ServiceDto findById(@PathVariable String id) {
        return mapper.mapToDto(
                manager.findByIdOrDie(id)
        );
    }

    @PostMapping
    ServiceDto create(@RequestBody ServiceCreationRequestDto dto) {
        return mapper.mapToDto(
                manager.create(
                        service -> mapper.fillFromDto(dto, service)
                )
        );
    }

    @PutMapping(value = "/{id}")
    ServiceDto update(@PathVariable String id,
                      @RequestBody ServiceUpdateRequestDto dto) {
        return mapper.mapToDto(
                manager.update(id, service -> mapper.fillFromDto(dto, service))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<ServiceDto> delete(@PathVariable String id) {
        return manager.deleteById(id)
                .map(mapper::mapToDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
