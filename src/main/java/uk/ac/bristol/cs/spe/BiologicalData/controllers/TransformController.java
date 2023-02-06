package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bristol.cs.spe.BiologicalData.*;
import uk.ac.bristol.cs.spe.BiologicalData.database.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller class for managing transform page capabilities.
 */
@Controller
public class TransformController {

    private final StorageService storageService;

    @Autowired
    PageRepository pageRepo;
    @Autowired
    JobRepository jobRepo;
    @Autowired
    AuthorRepository authorRepo;
    @Autowired
    UserRepository userRepo;

    /**
     * Constructor for the TransformController.
     *
     * @param storageService contains the methods to allow for creation and deletion of files
     */
    @Autowired
    public TransformController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Loads the transform page upon request and adds necessary fields to the model.
     *
     * @param model resources to be used within TransformController
     */
    @GetMapping("/transform")
    public String loadTransform(Model model, HttpSession session) {
        String userNew = (String) session.getAttribute("User");
        if (userNew != null) {
            User userAuthed = userRepo.getUserByEmail(userNew);
            if (userAuthed.getUserRole() != null) {

                setImageProperties(model, session);
                setTranscription(model, session);
                setAuthor(model, session);

                DateInfo d = new DateInfo();
                model.addAttribute("datewritten", d);

                return "transform";
            }
            else{
                return "403";
            }
        }
        
        return "403";
    }

    private void setImageProperties(Model model, HttpSession session) {
        String fn = (String)session.getAttribute("image");
        model.addAttribute("filename", fn);
        model.addAttribute("imageLeft", session.getAttribute("imageLeft"));
    }

    private void setTranscription(Model model, HttpSession session) {
        Transcription t = new Transcription();

        String selection = (String)session.getAttribute("selectionText");
        t.setText(Objects.requireNonNullElse(selection, ""));

        String name = (String)session.getAttribute("selectionName");
        t.setName(Objects.requireNonNullElse(name, ""));

        model.addAttribute("transcription", t);
    }

    private void setAuthor(Model model, HttpSession session) {
        AuthorInfo a = new AuthorInfo();

        Long authorID = (Long)session.getAttribute("authorID");
        a.setAuthorID(Objects.requireNonNullElse(authorID, 0).longValue());

        String authorForename = (String)session.getAttribute("authorForename");
        a.setAuthorForename(Objects.requireNonNullElse(authorForename, ""));

        String authorSurname = (String)session.getAttribute("authorSurname");
        a.setAuthorSurname(Objects.requireNonNullElse(authorSurname, ""));

        Integer authorTitle = (Integer)session.getAttribute("authorTitle");
        a.setAuthorTitle(Objects.requireNonNullElse(authorTitle, 0));

        model.addAttribute("authorInfo", a);
        model.addAttribute("authors", authorRepo.findAll());
    }

    /**
     * Upon request, swaps the locations of the image and text areas by changing the value of imageLeft.
     *
     * @param session information about the current session to be used within TransformControllers
     */
    @GetMapping("/transform/swap")
    public String swapImageLocation(HttpSession session) {
        Boolean currentImgLeft = (Boolean)session.getAttribute("imageLeft");
        session.setAttribute("imageLeft", !currentImgLeft);
        return "redirect:/transform";
    }

    /**
     * Uses machine learning to translate the handwritten notes into digital text.
     *
     */
    @GetMapping("/transform/translate")
    public String translateImageToText(HttpSession session) throws TesseractException, IOException {
        String filename = (String)session.getAttribute("image");
        File image = new File(BiologicalDataApplication.getProjectPath() + "/src/main/dynamic/" + filename);
        Tesseract tesseract = new Tesseract();
        String datapath = BiologicalDataApplication.getProjectPath().substring(0, BiologicalDataApplication.getProjectPath().lastIndexOf("BiologicalData")) + "tessdata";
        tesseract.setDatapath(datapath);
        tesseract.setPageSegMode(3); //page segmentation mode - default is 3
        tesseract.setLanguage("eng");
        try {
            session.setAttribute("selectionText", tesseract.doOCR(image));
        }
        catch(Error e) {
            return "redirect:/transform";
        }
        return "redirect:/transform";
    }

    /**
     * Extracts the text within the text area and stores it in a text file within the dynamic folder.
     *
     * @param transcription contains the text written in the text area
     * @throws IOException
     */
    @Transactional
    @PostMapping("/transform/submit/{overwrite}")
    public String submitTranscription(@PathVariable Integer overwrite, @ModelAttribute Transcription transcription, HttpSession session) throws IOException, ScriptException {
        //Update user session, fetch info from session if coming from overwrite page
        String filename = (String)session.getAttribute("image");
        String transcriptionFileName = "";
        String transcriptionText = "";
        if(transcription.getName() != null && transcription.getText() != null){
            session.setAttribute("selectionText", transcription.getText());
            session.setAttribute("selectionName", transcription.getName());
            transcriptionFileName = transcription.getName();
            transcriptionText = transcription.getText();
        }
        else{
            transcriptionFileName = (String)session.getAttribute("selectionName");
            transcriptionText = (String)session.getAttribute("selectionText");
        }
        if (filename != null)
        {
            Page page = pageRepo.getPageByName(filename);
            Timestamp t = new Timestamp(Calendar.getInstance().getTimeInMillis());
            boolean usesCustomName = false;
            if(!transcriptionFileName.equals("")){
                usesCustomName = true;
                transcriptionFileName += ".txt";
                assert page != null;
                //Detect any duplicates
                boolean duplicate = false;
                Job j = jobRepo.getJobByName(transcriptionFileName);
                if(j != null)
                    duplicate = true;
                //Confirm overwrite
                if(duplicate && overwrite == 0)
                    return "overwrite";
                //If you've approved an overwrite, delete job to add new job at end of function
                else if(duplicate && overwrite == 1){
                    jobRepo.deleteByJobID(j.getJobID());
                }
            }
            if(!usesCustomName){
                transcriptionFileName = filename + t.toString().replaceAll(":", "-") + ".txt";
            }
            Path path = Paths.get(new StorageProperties().getLocation() + "/" + transcriptionFileName);
            Files.write(path, Collections.singletonList(transcriptionText));
            User user = userRepo.getUserByEmail(UserSessionService.getUserEmail(session));
            Job j = new Job(user, page, t, transcriptionFileName);
            jobRepo.save(j);

            session.setAttribute("selectionText", "");
            session.setAttribute("selectionName", "");
            session.setAttribute("transcriptionName", transcriptionFileName);
        }
        return "redirect:/transform";
    }

    /**
     * Downloads the text file currently stored in dynamic to the local computer.
     *
     */
    @GetMapping("/transform/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadTranscription(HttpSession session) {
        String transcriptionName = (String)session.getAttribute("transcriptionName");
        Resource file = this.storageService.loadAsResource(transcriptionName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename()).body(file);
    }

    /**
     * Uploads the selected file from the user's local computer then saves it to the webapp instance and the database.
     *
     * @param file the image to be uploaded
     */
    @PostMapping("/transform/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Long userID = UserSessionService.getUserID(userRepo, request.getSession());
        String permission = userRepo.getRoleByID(userID);
        if(permission.equals("ROLE_USER"))
            return "403";
        ArrayList<String> acceptedFiletypes = new ArrayList<>(Arrays.asList("png", "jpeg", "jpg"));
        if (acceptedFiletypes.contains(FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase(Locale.ROOT))) {
            if (!doesImageExist(file.getOriginalFilename(), pageRepo)) {
                storageService.store(file);

                Page page = new Page();
                page.setPageFile(file.getOriginalFilename());
                pageRepo.save(page);

                request.getSession().
                        setAttribute("image", file.getOriginalFilename());
            }
        }
        return "redirect:/transform";
    }

    /**
     * Returns whether or not the given filename exists within the database already.
     *
     * @param filename the filename to check
     * @param pageRepo the database to check within
     * @return
     */
    private boolean doesImageExist(String filename, PageRepository pageRepo) {
        Page page = pageRepo.getPageByName(filename);
        if(page == null)
            return false;
        return true;
    }

    /**
     * Loads the page based upon its given ID into the transform page.
     *
     * @param pageID image ID within the database
     */
    @GetMapping(value = "/transform/page/{pageID}")
    public String loadImage(@PathVariable Long pageID, HttpServletRequest request) {
        Optional<Page> p = pageRepo.findById(pageID);
        if (p.isEmpty()) {
            return "redirect:/transform";
        } else {
            var imageFile = p.get().getPageFile();
            Path path = Paths.get(new StorageProperties().getLocation() + "/" + imageFile);
            if(Files.exists(path)) {
                request.getSession().setAttribute("image", imageFile);
            }
            else {
                request.getSession().setAttribute("image", null);
            }
            request.getSession().setAttribute("selectionText", "");
            request.getSession().setAttribute("selectionName", "");
            request.getSession().setAttribute("transcriptionName", null);
            for(Author a : authorRepo.findAll()){
                if(a == p.get().getAuthorID()){
                    request.getSession().setAttribute("authorID", a.getAuthorID());
                    break;
                }
            }
            return "redirect:/transform";
        }
    }

    /**
     * Loads the page and transcription based upon its given ID into the transform page.
     *
     * @param jobID image ID within the database
     */
    @GetMapping(value = "/transform/job/{jobID}")
    public String loadTranscription(@PathVariable Long jobID, HttpServletRequest request) {
        Optional<Job> j = jobRepo.findById(jobID);
        if (j.isEmpty()) {
            return "redirect:/transform";
        } else {
            Job job = j.get();
            loadImage(job.getPageID().getPageID(), request);
            String transcriptionName = job.getJobFile();
            request.getSession().setAttribute("transcriptionName", transcriptionName);
            Path path = Paths.get(new StorageProperties().getLocation() + "/" + transcriptionName);
            try {
                String text = Files.readString(path);
                String name = job.getJobFile().replaceAll(".txt", "");
                request.getSession().setAttribute("selectionText", text);
                request.getSession().setAttribute("selectionName", name);
            } catch (IOException i) {
                return "redirect:/transform";
            }
            return "redirect:/transform";
        }
    }

    @Transactional
    @PostMapping(value = "transform/author")
    public String updateAuthor(@ModelAttribute AuthorInfo authorInfo, HttpSession session){
        if(session.getAttribute("image") != null){
            Author author = null;
            session.setAttribute("authorForename", authorInfo.getAuthorForename());
            session.setAttribute("authorSurname", authorInfo.getAuthorSurname());
            session.setAttribute("authorTitle", authorInfo.getAuthorTitle());
            session.setAttribute("authorID", authorInfo.getAuthorID());
            //If creating a new author
            if(authorInfo.getAuthorID() == -1){
                author = new Author(authorInfo.getAuthorForename(), authorInfo.getAuthorSurname(), authorInfo.getAuthorTitle());
                authorRepo.save(author);
            }
            //If selecting from an existing author
            else{
                author = authorRepo.getAuthorByID(authorInfo.getAuthorID());
            }
            assert author != null;
            pageRepo.updateAuthor(author.getAuthorID(), (String)session.getAttribute("image"));
        }
        return "redirect:/transform";
    }

    @Transactional
    @PostMapping(value = "transform/date")
    public String updateDate(@ModelAttribute DateInfo dateInfo, HttpSession session){
        String currentPage = (String)session.getAttribute("image");
        pageRepo.updateDate(dateInfo.getDate(), currentPage);
        return "redirect:/transform";
    }
}
