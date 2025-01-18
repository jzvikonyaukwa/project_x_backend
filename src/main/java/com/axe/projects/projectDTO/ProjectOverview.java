package com.axe.projects.projectDTO;

import com.axe.projects.Project;
import com.axe.quotes.Quote;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectOverview {
    Project project;
    List<Quote> quotes;
}
