package swt6.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmployeeTest {

	@Before
	public void setUp() throws Exception {
		Mock.GenerateEntities();
	}

	@After
	public void tearDown() throws Exception {
		Mock.Clean();
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
