package uk.ac.bristol.cs.spe.BiologicalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import uk.ac.bristol.cs.spe.BiologicalData.database.AuthorRepository;
import uk.ac.bristol.cs.spe.BiologicalData.database.PageRepository;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import java.util.Arrays;

/**
 * Startpoint of the BiologicalData application.
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BiologicalDataApplication {

	@Autowired
	UserRepository userRepo;
	@Autowired
	PageRepository pageRepo;
	@Autowired
	AuthorRepository authorRepo;

	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BiologicalDataApplication.class, args);
	}

	public static String getProjectPath() {
		String workingPath = System.getProperty("user.dir");
		int index = workingPath.lastIndexOf("src");
		if (index < 0) {
			return workingPath;
		}
		return workingPath.substring(0, index);
	}

}
