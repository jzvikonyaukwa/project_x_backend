package com.axe.projects.projectDTO.project_overviewDTO;

import lombok.Data;

public record ProjectDTO(
     Long id,
     String name,
     ClientDTO client){}

