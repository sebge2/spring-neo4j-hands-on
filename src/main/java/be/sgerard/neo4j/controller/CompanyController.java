package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.CompanyMapper;
import be.sgerard.neo4j.mapper.TeamMapper;
import be.sgerard.neo4j.model.dto.company.CompanyCreationRequestDto;
import be.sgerard.neo4j.model.dto.company.CompanyDto;
import be.sgerard.neo4j.model.dto.company.CompanySummaryDto;
import be.sgerard.neo4j.model.dto.company.CompanyUpdateRequestDto;
import be.sgerard.neo4j.model.dto.team.RootTeamCreationRequestDto;
import be.sgerard.neo4j.model.dto.team.TeamSummaryDto;
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
    List<CompanySummaryDto> search(@RequestParam(required = false) String name) {
        return manager.search(name).stream()
                .map(companyMapper::mapToSummaryDto)
                .toList();
    }

    @GetMapping("/{id}")
    CompanySummaryDto findById(@PathVariable String id) {
        return companyMapper.mapToSummaryDto(
                manager.findByIdOrDie(id)
        );
    }

    @PostMapping
    CompanySummaryDto create(@RequestBody CompanyCreationRequestDto dto) {
        return companyMapper.mapToSummaryDto(
                manager.create(
                        company -> companyMapper.fillFromDto(dto, company)
                )
        );
    }

    @PutMapping(value = "/{id}")
    CompanySummaryDto update(@PathVariable String id,
                             @RequestBody CompanyUpdateRequestDto dto) {
        return companyMapper.mapToSummaryDto(
                manager.update(id, company -> companyMapper.fillFromDto(dto, company))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<CompanySummaryDto> delete(@PathVariable String id) {
        return manager.deleteById(id)
                .map(companyMapper::mapToSummaryDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{id}/root-team")
    TeamSummaryDto create(@PathVariable(name = "id") String companyId,
                          @RequestBody RootTeamCreationRequestDto dto) {
        return teamMapper.mapToSummaryDto(
                manager.createRootTeam(
                        companyId,
                        team -> teamMapper.fillFromDto(dto, team)
                )
        );
    }

    @PostMapping("/{id}/do-load-all-graph")
    CompanyDto loadAllGraph(@PathVariable String id) {
        return companyMapper.mapToDto(
                manager.findByIdOrDie(id)
        );
    }
}
