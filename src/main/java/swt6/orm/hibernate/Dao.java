package swt6.orm.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.Module;
import swt6.orm.domain.Project;

public class Dao {
	

	public static <T> void saveEntity(T entity) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			//entity = (T)session.merge(entity);
			session.saveOrUpdate(entity);
			tx.commit(); // session is closed automatically
		} catch (Exception e) {
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
			//empl = (Employee)session.merge(empl);
			
			for (LogbookEntry entry : entries) {
				empl.addLogbookEntry(entry);
			}
			
			//empl = (Employee)session.merge(empl);
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
			e.printStackTrace();

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
			e.printStackTrace();

			if (tx != null)
				tx.rollback();
		}
	}
	
	public static Employee getEmployee(Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		Employee empl = null;
		
		try {
			tx = session.beginTransaction();
			empl = (Employee)session.get(Employee.class, id);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
		
		return empl;
	}
	
	@SuppressWarnings("unchecked")
	public static void removeEmployee(Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			Employee empl = (Employee)session.load(Employee.class, id);
			
			empl.getLogbookEntries().clear();

			for (Project project : empl.getProjects()) {
				project.removeMember(empl);
			}
			
			for (Project project : empl.getLeaders()) {
				project.detachLeader();
			}
			
			session.delete(empl);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<LogbookEntry> getAllLogbooks() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		List<LogbookEntry> list = null;
		
		try {
			tx = session.beginTransaction();
			
			list = session.createQuery("select e from LogbookEntry e").list();
			
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if(tx != null)
				tx.rollback();
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Project> getAllProjects() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		List<Project> list = null;
		
		try {
			tx = session.beginTransaction();
			
			list = session.createQuery("select p from Project p").list();
			
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if(tx != null)
				tx.rollback();
		}
		
		return list;
	}
	
	public static void removeLogbookEntry(LogbookEntry entry) {
		if (entry == null)
			throw new IllegalArgumentException();

		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			
			Employee empl = entry.getEmployee();
			if (empl != null) {
				empl.removeLogbookEntry(entry);
			}
			entry.detachEmployee();

			session.delete(entry);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
	}
	
	public static void removeModule(Module module) {
		if (module == null)
			throw new IllegalArgumentException();

		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			
			module.detachProject();
			module = (Module)session.merge(module);

			session.delete(module);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
		
	}
	
	public static void removeProject(Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			Project project =  (Project)session.get(Project.class, id);
			
			for(Module mod : project.getModules()) {
				mod.detachProject();
			}
			project.getModules().clear();
			session.saveOrUpdate(project);
			session.delete(project);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
	}
	
	public static <T> void removeItem(Class c, Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			T item = (T)session.load(c, id);
			session.delete(item);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
	}
	
	public static <T> T getItemById(Class c, Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		T item = null;
		
		try {
			tx = session.beginTransaction();
			item = (T)session.get(c, id);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
		
		return item;
	}
	
	public static Module getModuleById(Long id) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		Module item = null;
		
		try {
			tx = session.beginTransaction();
			item = (Module)session.load(Module.class, id);
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			
			if (tx != null)
				tx.rollback();
		}
		
		System.out.println(item);
		
		return item;
	}
}
