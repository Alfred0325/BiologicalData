package uk.ac.bristol.cs.spe.BiologicalData;

import java.util.UUID;
//import javax.validation.Valid;
/**
 * util functions
 */
public class Util {

    /**
     *
     * @return random id
     */
    public static String GenUUID() {

        UUID uuid = UUID.randomUUID();
        String genID = uuid.toString().replace("-", "");

        return genID;
    }
}
