package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.PersonMapper;
import be.sgerard.neo4j.model.dto.person.PersonDto;
import be.sgerard.neo4j.model.dto.person.PersonUpdateRequestDto;
import be.sgerard.neo4j.service.person.PersonManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Person")
@RestController
@RequestMapping("/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonManager manager;
    private final PersonMapper mapper;

    @GetMapping
    List<PersonDto> findAll() {
        return manager.findAll().stream()
                .map(mapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    PersonDto findById(@PathVariable String id) {
        return mapper.mapToDto(
                manager.findByIdOrDie(id)
        );
    }

    @PutMapping(value = "/{id}")
    PersonDto update(@PathVariable String id,
                     @RequestBody PersonUpdateRequestDto dto) {
        return mapper.mapToDto(
                manager.update(id, person -> mapper.fillFromDto(dto, person))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<PersonDto> delete(@PathVariable String id) {
        return manager.deleteById(id)
                .map(mapper::mapToDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
