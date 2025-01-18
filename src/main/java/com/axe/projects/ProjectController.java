package com.axe.projects;

import com.axe.projects.projectDTO.*;
import com.axe.projects.projectDTO.project_overviewDTO.ProjectOverviewDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("projects-name-and-client")
    public List<ProjectNameAndClient> getProjectsNameAndClient() {
        return projectService.getProjectsNameAndClient();
    }

    @GetMapping("/client-projects/{clientId}")
    public List<ProjectDetails> getClientProjects(@PathVariable Long clientId) {
        return projectService.getClientProjects(clientId);
    }
    @GetMapping("summary")
    public List<ProjectSummary> getProjectSummary() {
        return projectService.getProjectsSummary();
    }

    @GetMapping("{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @GetMapping("{id}/overview")
    public ProjectOverviewDTO getProjectOverview(@PathVariable Long id) {
        return projectService.getProjectOverview(id);
    }

    @PostMapping("")
    public ResponseEntity<?> createProject(@RequestBody ProjectPOSTDTO project) {
        try {
            Project savedProject = projectService.createProject(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
        }
        catch (DataIntegrityViolationException e) {
            throw e;
        }
        catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
    }
    }
}
