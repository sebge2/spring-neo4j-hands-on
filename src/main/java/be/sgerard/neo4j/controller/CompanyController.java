package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.CompanyMapper;
import be.sgerard.neo4j.mapper.TeamMapper;
import be.sgerard.neo4j.model.dto.company.CompanyCreationRequestDto;
import be.sgerard.neo4j.model.dto.company.CompanyDto;
import be.sgerard.neo4j.model.dto.company.CompanyUpdateRequestDto;
import be.sgerard.neo4j.model.dto.team.RootTeamCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamDto;
import be.sgerard.neo4j.service.company.CompanyManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Company")
@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyManager manager;
    private final CompanyMapper companyMapper;
    private final TeamMapper teamMapper;

    @GetMapping
    List<CompanyDto> findAll() {
        return manager.findAll().stream()
                .map(companyMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    CompanyDto findById(@PathVariable String id) {
        return companyMapper.mapToDto(
                manager.findByIdOrDie(id)
        );
    }

    @PostMapping
    CompanyDto create(@RequestBody CompanyCreationRequestDto dto) {
        return companyMapper.mapToDto(
                manager.create(
                        company -> companyMapper.fillFromDto(dto, company)
                )
        );
    }

    @PutMapping(value = "/{id}")
    CompanyDto update(@PathVariable String id,
                      @RequestBody CompanyUpdateRequestDto dto) {
        return companyMapper.mapToDto(
                manager.update(id, company -> companyMapper.fillFromDto(dto, company))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<CompanyDto> delete(@PathVariable String id) {
        return manager.deleteById(id)
                .map(companyMapper::mapToDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{id}/root-team")
    TeamDto create(@PathVariable(name = "id") String companyId,
                   @RequestBody RootTeamCreationRequestDto dto) {
        return teamMapper.mapToDto(
                manager.createRootTeam(
                        companyId,
                        team -> teamMapper.fillFromDto(dto, team)
                )
        );
    }
}
