package swt6.test;

import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.Project;
import swt6.orm.hibernate.HibernateUtil;

public class Helpers {
	

	public static <T> void saveEntity(T entity) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(entity);
			tx.commit(); // session is closed automatically
		} catch (Exception e) {
			// error handling code
			e.printStackTrace();

			if (tx != null)
				tx.rollback();
		}
	}
	
	public static Employee addLogbookEntry(Employee empl, LogbookEntry... entries) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			empl = (Employee)session.merge(empl);
			
			for (LogbookEntry entry : entries) {
				empl.addLogbookEntry(entry);
			}
			
			session.saveOrUpdate(empl);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();

			if (tx != null)
				tx.rollback();
		}

		return empl;
	}

	public static void saveModule(Module module, Project project) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			project = (Project)session.merge(project);
			module.attachProject(project);
			session.saveOrUpdate(module);
			tx.commit(); // session is closed automatically
		} catch (Exception e) {
			// error handling code

			if (tx != null)
				tx.rollback();
		}
	}
	
	public static void saveProject(Project project, Employee leader) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			leader = (Employee)session.merge(leader);
			project.attachLeader(leader);
			session.saveOrUpdate(project);
			tx.commit(); // session is closed automatically
		} catch (Exception e) {
			// error handling code

			if (tx != null)
				tx.rollback();
		}
	}
}
