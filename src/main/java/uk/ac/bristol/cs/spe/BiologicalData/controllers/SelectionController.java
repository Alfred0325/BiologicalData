package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.bristol.cs.spe.BiologicalData.FilterInfo;
import uk.ac.bristol.cs.spe.BiologicalData.UserSessionService;
import uk.ac.bristol.cs.spe.BiologicalData.database.*;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Controller class for managing selection page capabilities.
 */
@Controller
public class SelectionController {

    @Autowired
    PageRepository pageRepo;
    @Autowired
    JobRepository jobRepo;
    @Autowired
    AuthorRepository authorRepo;
    @Autowired
    UserRepository userRepo;

    /**
     * Loads the selection page upon request and adds the saved transcription pages to the model.
     * @param model resources to be used within SelectionController
     */
    @GetMapping(value = "/selection")
    public String loadSelection(Model model, HttpSession session) {
        String userNew = (String) session.getAttribute("User");
        if (userNew != null) {
            User userAuthed = userRepo.getUserByEmail(userNew);
            if (userAuthed.getUserRole() != null) {
                FilterInfo f = getSessionFilterInfo(session);
                model.addAttribute("filterinfo", f);

                Iterable<Job> jobs;
                if(f.getShowOnlyUserJobs() == 1){
                    Long id = UserSessionService.getUserID(userRepo, session);
                    jobs = jobRepo.getJobByUser(id);
                }
                else{
                    jobs = jobRepo.findAll();
                }
                List<Page> pages;
                if(f.getShowAuthor() != 0){
                    pages = pageRepo.getPagesByAuthor(f.getShowAuthor());
                }
                else{
                    pages = pageRepo.getAll();
                }
                pages = sortPages(pages, f.getSortBy(), f.getDirection());
                pages = searchPages(pages, f.getImageName());

                model.addAttribute("pages", pages);
                model.addAttribute("jobs", jobs);
                model.addAttribute("authors", authorRepo.findAll());

                return "selection";
            }
        }

        return "403";
    }

    FilterInfo getSessionFilterInfo(HttpSession session){
        FilterInfo f = new FilterInfo();
        Integer ownJobs = (Integer)session.getAttribute("ownJobs");
        f.setShowOnlyUserJobs(Objects.requireNonNullElse(ownJobs, 0));
        Integer authorJobs = (Integer)session.getAttribute("authorJobs");
        f.setShowAuthor(Objects.requireNonNullElse(authorJobs, 0));
        Integer sortBy = (Integer)session.getAttribute("sortBy");
        f.setSortBy(Objects.requireNonNullElse(sortBy, 0));
        Integer direction = (Integer)session.getAttribute("direction");
        f.setDirection(Objects.requireNonNullElse(direction, 0));
        String imageName = (String)session.getAttribute("imageName");
        f.setImageName(Objects.requireNonNullElse(imageName, ""));
        return f;
    }

    List<Page> sortPages(List<Page> pages, int sortBy, int direction){
        Comparator<Page> c = null;
        switch(sortBy){
            case 1:
                c = (page, page2) -> page.getPageFile().compareToIgnoreCase(page2.getPageFile());
                break;
            case 2:
                c = (page, page2) -> {
                    Date page1Date = page.getDate();
                    Date page2Date = page2.getDate();
                    if(page1Date == null || page2Date == null)
                        return 0;
                    if(page1Date.equals(page2Date))
                        return 0;
                    return page1Date.after(page2Date) ? -1 : 1;
                };
                break;
        }
        if(c != null){
            pages.sort(c);
        }
        if(direction == 1){
            Collections.reverse(pages);
        }
        return pages;
    }

    List<Page> searchPages(List<Page> pages, String fileName){
        List<Page> newPages = new LinkedList<>();
        for(Page p : pages){
            String pageFileName = p.getPageFile().toLowerCase();
            String searchName = fileName.toLowerCase();
            if(pageFileName.contains(searchName)){
                newPages.add(p);
            }
        }
        return newPages;
    }

    @GetMapping(value = "/selection/deletejob/{jobID}")
    public String deleteTranscription(@PathVariable Long jobID, Model model){
        Job j = jobRepo.getJobByID(jobID);
        File file = new File("src/main/dynamic/" + j.getJobFile());
        file.delete();
        jobRepo.deleteById(j.getJobID());
        return "redirect:/selection";
    }

    @GetMapping(value = "/selection/deletepage/{pageID}")
    public String deletePage(@PathVariable Long pageID, Model model){
        Page pageToDelete = pageRepo.findById(pageID).orElse(null);
        if(pageToDelete == null)
            return "redirect:/selection";
        Collection<Job> jobsToDelete = jobRepo.getJobByPage(pageToDelete.getPageID());
        File page = new File("src/main/dynamic/" + pageToDelete.getPageFile());
        page.delete();
        for(Job j : jobsToDelete){
            File file = new File("src/main/dynamic/" + j.getJobFile());
            file.delete();
            jobRepo.deleteById(j.getJobID());
        }
        pageRepo.deleteById(pageID);
        return "redirect:/selection";
    }

    @GetMapping(value = "/selection/updatefilter")
    public String updateFilter(@ModelAttribute FilterInfo filterinfo, HttpSession session){
        //Save filter to session
        session.setAttribute("ownJobs", filterinfo.getShowOnlyUserJobs());
        session.setAttribute("authorJobs", filterinfo.getShowAuthor());
        session.setAttribute("sortBy", filterinfo.getSortBy());
        session.setAttribute("direction", filterinfo.getDirection());
        session.setAttribute("imageName", filterinfo.getImageName());

        return "redirect:/selection";
    }

    @GetMapping(value = "/selection/clearfilter")
    public String clearFilter(HttpSession session){
        session.setAttribute("ownJobs", 0);
        session.setAttribute("authorJobs", 0);
        session.setAttribute("sortBy", 0);
        session.setAttribute("direction", 0);
        session.setAttribute("imageName", "");

        return "redirect:/selection";
    }
}
