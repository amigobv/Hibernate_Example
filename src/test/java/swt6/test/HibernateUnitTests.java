package swt6.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.model.EachTestNotifier;

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

		List<Module> modules = Dao.getAllModules();
		assertNotNull(modules);
		
		Module mod = modules.get(0);
		assertNotNull(mod);
	
		List<Phase> phases = Dao.getAllPhases();
		assertNotNull(phases);
		
		Phase ph = phases.get(0);
		assertNotNull(ph);

		LogbookEntry entry = new LogbookEntry("Testing",
				DateUtil.getTime(12, 0), DateUtil.getTime(13, 30));
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
		Employee empl = Dao.getItemById(Employee.class,
				createTestEmployee().getId());

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
		Employee empl = Dao.getItemById(Employee.class,
				createTestEmployee().getId());

		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);

		Project proj = projects.get(0);
		assertNotNull(proj);

		proj.attachLeader(empl);
		Dao.saveEntity(empl);

		assertTrue(empl.getLeaders().contains(proj));

		Dao.removeEmployee(empl.getId());
	}

	@Test
	public void assignEntryWithNewPhase() {
		Phase ph = new Phase("Release");
		Dao.saveEntity(ph);

		List<Employee> employees = Dao.getAllEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(0);
		assertNotNull(empl);

		LogbookEntry entry = new LogbookEntry("Release",
				DateUtil.getTime(14, 0), DateUtil.getTime(16, 30));
		entry.setPhaseId(ph);
		empl.addLogbookEntry(entry);
		Dao.saveEntity(empl);

		Dao.removeLogbookEntry(entry);
		Dao.removeItem(Phase.class, ph.getId());
	}

	@Test
	public void assignEntryWithNewModule() {
		Module mod = new Module("Delivery");
		Dao.saveEntity(mod);

		List<Employee> employees = Dao.getAllEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(0);
		assertNotNull(empl);

		LogbookEntry entry = new LogbookEntry("Release",
				DateUtil.getTime(14, 0), DateUtil.getTime(16, 30));
		entry.setModuleId(mod);
		empl.addLogbookEntry(entry);
		Dao.saveEntity(empl);

		Dao.removeLogbookEntry(entry);
		Dao.removeItem(Module.class, mod.getId());
	}

	@Test
	public void removeLogbookEntryFromEmployee() {
		List<Employee> employees = Dao.getAllEmployees();
		assertNotNull(employees);

		Employee empl = employees.get(employees.size()-2);
		assertNotNull(empl);

		List<LogbookEntry> entries = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entries);

		LogbookEntry entry = entries.get(0);
		assertNotNull(entry);

		Dao.removeLogbookEntry(entry);
		List<LogbookEntry> entriesAfterDelete = Dao
				.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entriesAfterDelete);

		assertEquals(1, entries.size() - entriesAfterDelete.size());
	}

	@Test
	public void insertNewProject() {
		Employee empl = Dao.getItemById(Employee.class,createTestEmployee().getId());

		Project proj = new Project("Unit Test");
		Dao.saveProject(proj, empl);
		
		List<Project> projects = Dao.getAllProjects();
		assertNotNull(projects);
		assertTrue(projects.contains(proj));

		Dao.removeEmployee(empl.getId());
	}
	
	@Test
	public void removeEmployee() {
		List<Employee> employees = Dao.getAllEmployees();
		assertNotNull(employees);
		
		Employee empl = employees.get(1);
		assertNotNull(empl);
		
		List<LogbookEntry> entries = Dao.getLogbooksFromEmployee(empl.getId());
		assertNotNull(entries);
		
		for(LogbookEntry entry : entries) {
			entry.detachEmployee();
		}
		
		Dao.removeEmployee(empl.getId());
		
		int oldSize = employees.size();
		employees.clear();
		employees = Dao.getAllEmployees();
		assertNotNull(employees);
		assertEquals(oldSize-1, employees.size());
	}

	@Test
	public void changeEmployeeAddress() {
		List<Employee> employees = Dao.getAllEmployees();
		assertNotNull(employees);
		
		Employee empl = employees.get(0);
		assertNotNull(empl);
		
		empl.setAddress(new Address("6969", "London", "Bakers Street 4"));
		Dao.updateEmployee(empl);
		
		Employee emplLoad = Dao.getEmployee(empl.getId());
		assertEquals(empl.getAddress(), emplLoad.getAddress());
	}

	private Employee createTestEmployee() {
		PermanentEmployee empl = new PermanentEmployee("Daniel", "Rotaru",
				DateUtil.getDate(1984, 7, 27));
		empl.setAddress(new Address("4020", "Linz", "Hauptstraße 1"));
		empl.setSalary(3000.0);

		Dao.saveEntity(empl);

		return empl;
	}
}
