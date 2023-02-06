package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;

public class AuthorInfo {

    @Getter @Setter
    private Long authorID;

    @Getter @Setter
    private String authorForename;

    @Getter @Setter
    private String authorSurname;

    @Getter @Setter
    private int authorTitle;
}
