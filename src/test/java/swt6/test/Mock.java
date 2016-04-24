package swt6.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Phase;
import swt6.orm.domain.Project;
import swt6.orm.domain.TemporaryEmployee;
import swt6.orm.hibernate.HibernateUtil;
import swt6.util.DateUtil;

public class Mock {
	private static final int NUM_OF_EMPLOYEES = 10;
	private static final int NUM_OF_LOGBOOKS = 5;
	private static final int NUM_OF_PROJECTS = 3;
	
	private static final String[] SURNAMES = {
		"Franz", "Johann", "Philipp", "Florian", "Felix", 
		"Tim", "Mathias", "Paul", "Max", "Tobias", "Laura",
		"Lisa", "Alexandra", "Sarah", "Anna", "Sophie"
	};
	private static final String[] NAMES = {
		"Berger", "Maier", "Fischer", "Bauer", "Koch", "Schmid",
		"Hofmann", "Wagner", "Spanring", "Müller", "Schneider",
		"Lehmann", "Mayr", "Klein", "Becker", "Fuchs"
	};
	private static final Date[] DATES = {
		DateUtil.getDate(1980, 7, 12),
		DateUtil.getDate(1971, 3, 2),
		DateUtil.getDate(1965, 5, 6),
		DateUtil.getDate(1975, 9, 15),
		DateUtil.getDate(1984, 8, 13),
		DateUtil.getDate(1990, 1, 1),
		DateUtil.getDate(1986, 11, 30),
		DateUtil.getDate(1988, 5, 24),
		DateUtil.getDate(1977, 6, 29),
		DateUtil.getDate(1976, 6, 16),
		DateUtil.getDate(1985, 9, 15),
		DateUtil.getDate(1981, 10, 13),
		DateUtil.getDate(1982, 12, 25),
		DateUtil.getDate(1979, 3, 17),
		DateUtil.getDate(1991, 4, 8),
	};
	private static final Address[] ADDRESSES = {
		new Address("4020", "Linz", "Hauptstraße 1"),
		new Address("4020", "Linz", "Gruberstraße 5"),
		new Address("4040", "Linz", "Herrenstreße 15"),
		new Address("4030", "Linz", "Freistädterstraße 3"),
		new Address("4020", "Linz", "Altenbergerstraße 7"),
		new Address("4020", "Linz", "Salzburgerstraße 45"),
		new Address("4020", "Linz", "Landwiedstraße 5"),
		new Address("4040", "Linz", "Europastraße 13"),
		new Address("4020", "Linz", "Weißenwolfstraße 5"),
		new Address("4020", "Linz", "Franckstraße 56"),
		new Address("4030", "Linz", "Wienerstraße 78"),
		new Address("4020", "Linz", "Froschberg 46"),
		new Address("4040", "Linz", "Fichtenstraße 2"),
		new Address("4020", "Linz", "Flachenauergutstraße 1"),
	};
	
	private static final double[] SALARIES = {
		2300,
		1800,
		2500,
		2800,
		3100,
		1950,
		2400,
		2000,
		3000,
		3500,
		3400,
		3100,
		2900,	
	};
	
	private static final String[] ACTIVITIES = {
		"Design",
		"Implementation",
		"Testing",
		"Research",
		"Meetings",
		"Documentation",
		"Non productiv",
	};
	
	private static final String[] PROJECTS = {
		"Blue Monkey",
		"Purple Crow",
		"Turtle",
	};
	
	private static Employee[] pEmpl;
	private static Employee[] tEmpl;
	private static Project[] projects;
	private static Module module;
	private static Phase phase;
	
	public static void GenerateEntities() {
		System.out.println("Generate entities!");
		
		CreateEmployees();
		CreateProjects();
		CreateModules();
		CreatePhases();
		CreateLogbooks();
	}
	
	private static void CreateEmployees() {
		System.out.println("Employees: \n\r");
		
		pEmpl = new Employee[NUM_OF_EMPLOYEES];
		tEmpl = new Employee[NUM_OF_EMPLOYEES];
		
		for (int i = 0; i < NUM_OF_EMPLOYEES-3; i++) {
			PermanentEmployee empl = new PermanentEmployee(SURNAMES[i],
														   NAMES[i], 
														   DATES[i]);
			empl.setAddress(ADDRESSES[i]);
			empl.setSalary(SALARIES[i]);
			pEmpl[i] = empl;
			Helpers.saveEntity(empl);
		}

		for (int i = 0; i < NUM_OF_EMPLOYEES-7; i++) {
			TemporaryEmployee empl = new TemporaryEmployee(SURNAMES[i+7],
														   NAMES[i+7], 
														   DATES[i+7]);
			empl.setAddress(ADDRESSES[i+7]);
			empl.setHourlyRate(50.0);
			empl.setRenter("Microsoft");
			empl.setStartDate(DateUtil.getDate(2016, 4+i, 1));
			empl.setEndDate(DateUtil.getDate(2016, 5+i, 1));
			tEmpl[i] = empl;
			Helpers.saveEntity(empl);
		}
	}
	
	private static void CreateProjects() {
		System.out.println("Projects: \n\r");
		projects = new Project[NUM_OF_PROJECTS];
		
		for (int i = 0; i < NUM_OF_PROJECTS; i++) {
			Project p = new Project(PROJECTS[i]);
			projects[i] = p;
			Helpers.saveProject(p, pEmpl[1]);	
		}
	}
	
	private static void CreateModules() {
		System.out.println("Modules: \n\r");
		
		module = new Module("Prototype");
		Helpers.saveModule(module, projects[0]);
	}
	
	private static void CreatePhases() {
		System.out.println("Phases: \n\r");
		
		phase = new Phase("Development");
		Helpers.saveEntity(phase);
	}
	
	private static void CreateLogbooks() {
		System.out.println("Logbooks: \n\r");
		
		for(int i = 0; i < NUM_OF_EMPLOYEES; i++) {
			if (pEmpl[i] != null) {
				for (int j = 0; j < NUM_OF_LOGBOOKS; j++) {
					LogbookEntry entry = new LogbookEntry(ACTIVITIES[j], DateUtil.getTime(8+j, i), DateUtil.getTime(14+j, 30+i), phase, module);
					pEmpl[i] = Helpers.addLogbookEntry(pEmpl[i], entry);
				}
			}
			
			if (tEmpl[i] != null) {
				for (int j = 0; j < NUM_OF_LOGBOOKS; j++) {
					LogbookEntry logbook = new LogbookEntry(ACTIVITIES[j], DateUtil.getTime(9+j, 0), DateUtil.getTime(15+j, 30), phase, module);
					tEmpl[i] = Helpers.addLogbookEntry(tEmpl[i], logbook);
				}
			}
		}
	}
	
	public static void Clean() {
		System.out.println("Clean entities!");
		
		
		CleanLogbooks();
		CleanPhases();
		CleanModules();
		CleanProjects();
		CleanEmployees();
	}

	private static void CleanLogbooks() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction(); 
			
			for(int i = 0; i < NUM_OF_EMPLOYEES; i++) {
				if (tEmpl[i] != null) {
					for (LogbookEntry entry : tEmpl[i].getLogbookEntries()) {
						tEmpl[i].removeLogbookEntry(entry);
						session.delete(entry);
					}
				}
				
				if (pEmpl[i] != null) {
					for (LogbookEntry entry : pEmpl[i].getLogbookEntries()) {
						tEmpl[i].removeLogbookEntry(entry);
						session.delete(entry);
					}
					session.delete(pEmpl[i]);
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

	private static void CleanPhases() {
		Session session = HibernateUtil.getCurrentSession();		
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction(); 
			
			session.delete(phase);
			tx.commit();
		} catch(Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

	private static void CleanModules() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			session.delete(module);
			tx.commit();
		} catch(Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

	private static void CleanProjects() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			for (int i = 0; i < NUM_OF_PROJECTS; i++) {
				session.delete(projects[i]);
			}
			tx.commit();
		} catch(Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}

	private static void CleanEmployees() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			for (int i = 0; i < NUM_OF_EMPLOYEES; i++) {
				if (tEmpl[i] != null) {
					session.delete(tEmpl[i]);
				}
				
				if (pEmpl[i] != null) {
					session.delete(pEmpl[i]);
				}
			}
			tx.commit();
		} catch(Exception e) {
			if (tx != null)
				tx.rollback();
		}
	}
}
