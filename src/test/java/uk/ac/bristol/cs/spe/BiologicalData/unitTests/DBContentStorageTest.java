package uk.ac.bristol.cs.spe.BiologicalData.unitTests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bristol.cs.spe.BiologicalData.FileStorageService;
import uk.ac.bristol.cs.spe.BiologicalData.StorageProperties;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

/**
 * test the storage function with content
 */
@SpringBootTest
public class DBContentStorageTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void storageTest() {
        StorageProperties fakeStorageProperties = new StorageProperties();
        fakeStorageProperties.setLocation("src/test/resources/mockfile.tmp");
        FileStorageService storageService = new FileStorageService(fakeStorageProperties);
        storageService.init();

        try {
            MultipartFile file = new MockMultipartFile("mockTxt", "Hello World".getBytes());
            storageService.store(file);
            System.out.println("load the file successfully");
        }catch(Exception e) {
            System.out.println("unable to load empty file");
        }
    }
}
