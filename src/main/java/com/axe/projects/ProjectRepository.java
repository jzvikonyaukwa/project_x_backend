package com.axe.projects;

import com.axe.projects.projectDTO.ProjectDetails;
import com.axe.projects.projectDTO.ProjectNameAndClient;
import com.axe.projects.projectDTO.ProjectOverview;
import com.axe.projects.projectDTO.ProjectSummary;
import com.axe.quotes.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p.id as id, p.name as name FROM Project p WHERE p.client.id = ?1")
    List<ProjectDetails> findClientProjects(Long clientId);
//
//    @Query(value ="""
//            SELECT
//                 p.id,
//                 p.name AS projectName,
//                 c.name AS clientName,
//                 SUM(CASE WHEN q.status = 'accepted' THEN 1 ELSE 0 END) AS acceptedQuotes,
//                 SUM(CASE WHEN cl.status = 'completed' THEN 1 ELSE 0 END) AS completedCuttingLists,
//                 SUM(CASE WHEN cl.status = 'scheduled' THEN 1 ELSE 0 END) AS scheduledCuttingLists,
//                 COUNT(cl.id) AS totalCuttingLists
//             FROM axe.projects p
//             JOIN axe.clients c ON c.id = p.client_id
//             JOIN axe.quotes q ON q.project_id = p.id
//             JOIN axe.cutting_lists cl ON cl.quote_id = q.id
//             GROUP BY p.id, p.name, c.name
//             ORDER BY p.id DESC ;
//        """, nativeQuery = true)
//    List<ProjectSummary> getProjectsSummary();


    @Query(value = """
    SELECT
         p.id,
         p.name AS projectName,
         c.name AS clientName,
         SUM(CASE WHEN q.status = 'accepted' THEN 1 ELSE 0 END) AS acceptedQuotes,
         SUM(CASE WHEN pr.status = 'completed' THEN 1 ELSE 0 END) AS completedCuttingLists,
         SUM(CASE WHEN pr.status = 'scheduled' THEN 1 ELSE 0 END) AS scheduledCuttingLists,
         COUNT(pr.id) AS totalCuttingLists,
         COUNT(cons.id) AS totalConsumables
     FROM axe.projects p
     JOIN axe.clients c ON c.id = p.client_id
     JOIN axe.quotes q ON q.project_id = p.id
     LEFT JOIN axe.products pr ON pr.quote_id = q.id
     LEFT JOIN axe.consumables_on_quote cons ON cons.quote_id = q.id
     GROUP BY p.id, p.name, c.name
     ORDER BY p.id DESC;
""", nativeQuery = true)
    List<ProjectSummary> getProjectsSummary();

    @Query("SELECT q FROM Quote q WHERE q.project.id = ?1")
    List<Quote> getQuotesForProject(Long id);

    // order by p.name
    @Query("SELECT p.id as id, p.name as projectName, c.name as clientName FROM Project p JOIN p.client c")
    List<ProjectNameAndClient> getProjectsNameAndClient();
}
