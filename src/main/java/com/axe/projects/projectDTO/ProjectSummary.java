package com.axe.projects.projectDTO;

public interface ProjectSummary {
    Long getId();
    String getProjectName();
    String getClientName();
    Integer getAcceptedQuotes();
    Integer getCompletedCuttingLists();
    Integer getScheduledCuttingLists();
    Integer getTotalCuttingLists();
}
