package swt6.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Phase;
import swt6.orm.domain.Project;
import swt6.orm.hibernate.Dao;
import swt6.util.DateUtil;

public class HibernateUnitTests {

	@Before
	public void setUp() throws Exception {
		Mock.GenerateEntities();
	}

	@After
	public void tearDown() throws Exception {
		Mock.Clean();
	}

	@Test
	public void insertNewEmployeeTest() {
		Employee empl = createTestEmployee();

		Employee dbEmpl = Dao.getEmployee(empl.getId());
		assertNotNull(dbEmpl);

		if (empl.getId() != dbEmpl.getId())
			fail("Id are not matching");

		Dao.removeEmployee(dbEmpl.getId());
	}

	@Test
	public void insertNewLogbookEntry() {
		Employee empl = Dao.getEmployee(createTestEmployee().getId());

		Module mod = Dao.getItemById(Module.class, 1L);
		assertNotNull(mod);

		Phase ph = Dao.getItemById(Phase.class, 1L);
		assertNotNull(ph);

		LogbookEntry entry = new LogbookEntry("Testing", DateUtil.getTime(12, 0), DateUtil.getTime(13, 30));
		empl = Dao.addLogbookEntry(empl, entry);
		assertNotNull(empl);

		LogbookEntry dbEntry = Dao.getItemById(LogbookEntry.class,
				entry.getId());
		assertNotNull(dbEntry);
		assertEquals(entry.getId(), dbEntry.getId());
		assertEquals(entry.getActivity(), dbEntry.getActivity());

		Dao.removeLogbookEntry(entry);
		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void assignEmployeeToProject() {
		Employee empl = Dao.getItemById(Employee.class, createTestEmployee().getId());
		
		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);
		
		Project proj = projects.get(0);
		assertNotNull(proj);
		
		empl.addProject(proj);
		Dao.saveEntity(empl);
		
		assertTrue(empl.getProjects().contains(proj));
		
		Dao.removeEmployee(empl.getId());	
	}

	 @Test
	 public void assignEmployeeAsProjectLeader() {
	
	 }
	//
	// @Test
	// public void assignEntryWithNewPhase() {
	//
	// }
	//
	// @Test
	// public void assignEntryWithNewModule() {
	//
	// }
	//
	// @Test
	// public void removeLogbookEntryFromEmployee() {
	//
	// }
	//
	// @Test
	// public void removeEmployeeFromProject() {
	//
	// }
	//
	// @Test
	// public void assignEmployeeToOtherProject() {
	//
	// }
	//
	// @Test
	// public void changeEmployeeAddress() {
	//
	// }

	private Employee createTestEmployee() {
		PermanentEmployee empl = new PermanentEmployee("Daniel", "Rotaru",
				DateUtil.getDate(1984, 7, 27));
		empl.setAddress(new Address("4020", "Linz", "Hauptstraße 1"));
		empl.setSalary(3000.0);

		Dao.saveEntity(empl);

		return empl;
	}
}
