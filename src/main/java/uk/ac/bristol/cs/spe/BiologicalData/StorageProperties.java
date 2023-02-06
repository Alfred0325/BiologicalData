package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class that contains the storage location used for dynamic content.
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Getter @Setter
    private String location = BiologicalDataApplication.getProjectPath() + "/src/main/dynamic";

}