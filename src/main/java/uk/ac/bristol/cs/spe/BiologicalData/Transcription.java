package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that stores the necessary information for storing a transcription.
 */
public class Transcription {

    /**
     * String that contains the written transcription.
     */
    @Getter @Setter
    private String text;

    @Getter @Setter
    private String name;
}
