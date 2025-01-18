package com.axe.projects.projectDTO.project_overviewDTO;



import java.util.List;


public record ProjectOverviewDTO(ProjectDTO project, List<QuoteDTO> quotes){}
