package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;

public class FilterInfo {

    @Getter
    @Setter
    private int showOnlyUserJobs;

    @Getter
    @Setter
    private int showAuthor;

    @Getter
    @Setter
    private int sortBy;

    @Getter
    @Setter
    private int direction;

    @Getter
    @Setter
    private String imageName;
}
