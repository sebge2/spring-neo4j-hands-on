package be.sgerard.neo4j.controller;

import be.sgerard.neo4j.mapper.PersonMapper;
import be.sgerard.neo4j.mapper.ProjectMapper;
import be.sgerard.neo4j.mapper.TeamMapper;
import be.sgerard.neo4j.mapper.TeamMemberMapper;
import be.sgerard.neo4j.model.dto.project.ProjectCreationRequestDto;
import be.sgerard.neo4j.model.dto.project.ProjectSummaryDto;
import be.sgerard.neo4j.model.dto.team.*;
import be.sgerard.neo4j.service.team.TeamManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Team")
@RestController
@RequestMapping("/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamManager teamManager;

    private final TeamMapper teamMapper;
    private final TeamMemberMapper memberMapper;
    private final PersonMapper personMapper;
    private final ProjectMapper projectMapper;

    @GetMapping
    List<TeamSummaryDto> findAll() {
        return teamManager.findAll().stream()
                .map(teamMapper::mapToSummaryDto)
                .toList();
    }

    @GetMapping("/{id}")
    TeamSummaryDto findById(@PathVariable String id) {
        return teamMapper.mapToSummaryDto(
                teamManager.findByIdOrDie(id)
        );
    }

    @GetMapping("/{id}/sub-teams")
    List<TeamSummaryDto> findAllSubTeams(@PathVariable(name = "id") String parentTeamId) {
        return teamManager.findAllSubTeams(parentTeamId).stream()
                .map(teamMapper::mapToSummaryDto)
                .toList();
    }

    @PostMapping("/{id}/sub-teams")
    TeamSummaryDto createSubTeam(@RequestBody SubTeamCreationRequestDto dto,
                                 @PathVariable(name = "id") String parentTeamId) {
        return teamMapper.mapToSummaryDto(
                teamManager.createSub(
                        parentTeamId,
                        team -> teamMapper.fillFromDto(dto, team)
                )
        );
    }

    @PutMapping(value = "/{id}")
    TeamSummaryDto update(@PathVariable String id,
                          @RequestBody TeamUpdateRequestDto dto) {
        return teamMapper.mapToSummaryDto(
                teamManager.update(id, team -> teamMapper.fillFromDto(dto, team))
        );
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<TeamSummaryDto> delete(@PathVariable String id) {
        return teamManager.deleteById(id)
                .map(teamMapper::mapToSummaryDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}/members")
    List<TeamMemberDto> findAllMembers(@PathVariable(name = "id") String teamId) {
        return teamManager.findAllMembers(teamId).stream()
                .map(memberMapper::mapToDto)
                .toList();
    }

    @PostMapping("/{id}/members")
    TeamMemberDto createAddMember(@PathVariable(name = "id") String teamId,
                                  @RequestBody PersonCreationMemberRequestDto dto) {
        return memberMapper.mapToDto(
                teamManager
                        .createAddMember(
                                teamId,
                                member -> memberMapper.fillFromDto(dto, member),
                                person -> personMapper.fillFromDto(dto.getPerson(), person)
                        )
        );
    }

    @PutMapping("/{id}/members/{personId}")
    TeamMemberDto updateMember(@PathVariable(name = "id") String teamId,
                               @PathVariable(name = "personId") String personId,
                               @RequestBody TeamMemberUpdateRequestDto dto) {
        return memberMapper.mapToDto(
                teamManager.updateMember(teamId, personId, member -> memberMapper.fillFromDto(dto, member))
        );
    }

    @DeleteMapping("/{id}/members/{personId}")
    ResponseEntity<TeamMemberDto> deleteMember(@PathVariable(name = "id") String teamId,
                                               @PathVariable(name = "personId") String personId) {
        return teamManager.deleteMember(teamId, personId)
                .map(memberMapper::mapToDto)
                .map(ResponseEntity::ofNullable)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}/projects")
    List<ProjectSummaryDto> findAllProjects(@PathVariable(name = "id") String teamId) {
        return teamManager.findAllProjects(teamId).stream()
                .map(projectMapper::mapToSummaryDto)
                .toList();
    }

    @PostMapping("/{id}/projects")
    ProjectSummaryDto createProject(@PathVariable(name = "id") String teamId,
                                    @RequestBody ProjectCreationRequestDto dto) {
        return projectMapper.mapToSummaryDto(
                teamManager.addProject(
                        teamId,
                        project -> projectMapper.fillFromDto(dto, project)
                )
        );
    }
}
