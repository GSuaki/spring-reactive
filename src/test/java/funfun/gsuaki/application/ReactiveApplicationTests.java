package funfun.gsuaki.application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactiveApplication.class)
@ActiveProfiles("test")
public class ReactiveApplicationTests {

	@Test
	public void contextLoads() {
	}

}
