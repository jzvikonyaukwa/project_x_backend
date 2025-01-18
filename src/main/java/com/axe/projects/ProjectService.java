package com.axe.projects;

import com.axe.clients.ClientService;
import com.axe.projects.projectDTO.*;
import com.axe.projects.projectDTO.project_overviewDTO.ClientDTO;
import com.axe.projects.projectDTO.project_overviewDTO.ProjectDTO;
import com.axe.projects.projectDTO.project_overviewDTO.ProjectOverviewDTO;
import com.axe.projects.projectDTO.project_overviewDTO.QuoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final ClientService clientService;

    Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(ProjectRepository projectRepository, ClientService clientService) {
        this.projectRepository = projectRepository;
        this.clientService = clientService;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public List<ProjectDetails> getClientProjects(Long clientId) {
        return projectRepository.findClientProjects(clientId);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public Project createProject(ProjectPOSTDTO project) {
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setClient(clientService.getClientById(project.getClientId()));
        return projectRepository.save(newProject);
    }

    public List<ProjectSummary> getProjectsSummary() {
        return projectRepository.getProjectsSummary();
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

//    public ProjectOverview getProjectOverview(Long id) {
//
//        logger.info("Getting project overview for project with id: {}", id);
//        Project project = projectRepository.findById(id).orElse(null);
//        logger.info("Found project: {}", project);
//        List<Quote> quotes = projectRepository.getQuotesForProject(id);
//        logger.info("Found quotes: {}", quotes.size());
//        for (Quote quote : quotes) {
//            for (CuttingList cl : quote.getCuttingLists()){
//                cl.setManufacturedProducts(new ArrayList<>());
//            }
//            quote.setProject(null);
//        }
//
//        ProjectOverview projectOverview = new ProjectOverview();
//        projectOverview.setProject(project);
//        projectOverview.setQuotes(quotes);
//
//        return projectOverview;
//    }

    public ProjectOverviewDTO getProjectOverview(Long id) {
    Project project = projectRepository
            .findById(id)
            .orElseThrow(()-> new RuntimeException("Project does not Exist"));

    ClientDTO clientDTO = new ClientDTO(project.getClient().getId(),project.getClient().getName());
    ProjectDTO projectDTO = new ProjectDTO(project.getId(),project.getName(),clientDTO);
    List<QuoteDTO> quoteDTOs = projectRepository
            .getQuotesForProject(id)
            .stream()
            .map(quote -> QuoteDTO.builder()
            .id(quote.getId())
            .status(quote.getStatus().getValue())
            .dateIssued(quote.getDateIssued())
            .dateAccepted(quote.getDateAccepted())
            .products(quote.getProducts())
            .consumableOnQuotes(quote.getConsumablesOnQuote())
            .build()).toList();

    return new ProjectOverviewDTO(projectDTO, quoteDTOs);
}

    public List<ProjectNameAndClient> getProjectsNameAndClient() {
        List<ProjectNameAndClient> projects = projectRepository.getProjectsNameAndClient();
        // order projects by name
        projects.sort(Comparator.comparing(ProjectNameAndClient::getProjectName));
        return projects;
    }
}
