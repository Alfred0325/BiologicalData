package uk.ac.bristol.cs.spe.BiologicalData;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.bristol.cs.spe.BiologicalData.database.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringBootTest
class BiologicalDataApplicationTests {

	@Autowired
	BiologicalDataApplication bd;

	private UserRepository userRepo;
	private PageRepository pageRepo;
	private AuthorRepository authorRepo;

	void contextLoads() {
		userRepo = bd.userRepo;
		pageRepo = bd.pageRepo;
		authorRepo = bd.authorRepo;
	}

	@Test
	public void DBSaveTest(){
		contextLoads();
		User u = new User("test@test.org", "password");
		userRepo.save(u);
		User saved = userRepo.getUserByEmail("test@test.org");
		userRepo.deleteById(saved.getUserID());
		assert saved.getPassword().equals("password");
		assert saved.getUserEmail().equals("test@test.org");
	}

	@Test
	public void DBDeleteTest(){
		contextLoads();
		User u = new User("test@test.org", "password");
		userRepo.save(u);
		User saved = userRepo.getUserByEmail("test@test.org");
		userRepo.deleteById(saved.getUserID());
		saved = userRepo.getUserByEmail("test@test.org");
		assert saved == null;
	}
}
