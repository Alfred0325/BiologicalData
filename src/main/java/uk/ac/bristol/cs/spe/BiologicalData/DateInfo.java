package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class DateInfo{

    @Getter
    @Setter
    private Integer dayWritten;
    @Getter
    @Setter
    private Integer monthWritten;
    @Getter
    @Setter
    private Integer yearWritten;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}