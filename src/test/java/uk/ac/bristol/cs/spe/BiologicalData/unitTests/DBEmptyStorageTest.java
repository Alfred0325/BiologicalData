package uk.ac.bristol.cs.spe.BiologicalData.unitTests;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.bristol.cs.spe.BiologicalData.FileStorageService;
import uk.ac.bristol.cs.spe.BiologicalData.StorageProperties;
/***
 * database storage function without content
 */
@SpringBootTest
public class DBEmptyStorageTest {

    @Test
    public void storageTest() {

        StorageProperties fakeStorageProperties = new StorageProperties();

        FileStorageService storageService = new FileStorageService(fakeStorageProperties);

        try {
            MultipartFile file = null;
            storageService.store(file);
        }catch(Exception e) {
            System.out.println("unable to load empty file");
        }

    }
}
