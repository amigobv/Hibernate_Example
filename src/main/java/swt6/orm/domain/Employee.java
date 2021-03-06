package swt6.orm.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private Address address;
	private Set<LogbookEntry> logbookEntries = new HashSet<>();
	private Set<Project> projects = new HashSet<>();
	private Set<Project> leaders = new HashSet<>();

	// classes persisted by Hibernate must have a default constructor
	// (newInstance of reflection API)
	public Employee() {
	}

	public Employee(String firstName, String lastName, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}

	public Employee(String firstName, String lastName, Date dateOfBirth, Address address) {
		this(firstName, lastName, dateOfBirth);
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<LogbookEntry> getLogbookEntries() {
		return logbookEntries;
	}

	@SuppressWarnings("unused")
	private void setLogbookEntries(Set<LogbookEntry> logbookEntries) {
		this.logbookEntries = logbookEntries;
	}

	public void addLogbookEntry(LogbookEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("LogBookEntry must be null!");
		}

		if (entry.getEmployee() != null) {
			entry.getEmployee().logbookEntries.remove(entry);
		}

		this.logbookEntries.add(entry);
		entry.attachEmployee(this);
	}

	public void removeLogbookEntry(LogbookEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("LogBookEntry must be null!");
		}

		if (entry.getEmployee() != this)
			throw new IllegalArgumentException("can't remove logbook entry of another employee!");

		logbookEntries.remove(entry);
	}
	
	public void removeAllLogbookEntries() {
		Iterator<LogbookEntry> iter = logbookEntries.iterator();
		
		while(iter.hasNext()) {
			iter.remove();
			iter.next();
		}
	}

	public Set<Project> getProjects() {
		return projects;
	}

	@SuppressWarnings("unused")
	private void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public void addProject(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("Null Project");
		}
		project.getMembers().add(this);
		projects.add(project);
	}

	public void removeProject(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("NULL Project");
		}
		project.getMembers().remove(this);
		projects.remove(project);
	}
	
	public void detachProject() {
		
	}

	public Set<Project> getLeaders() {
		return leaders;
	}

	public void setLeaders(Set<Project> leaders) {
		this.leaders = leaders;
	}

	public void addLeader(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("NULL Project");
		}
		project.attachLeader(this);
		//leaders.add(project);
	}

	public void removeLeader(Project project) {
		if (project == null) {
			throw new IllegalArgumentException("NULL Project");
		}
		project.detachLeader();
		leaders.remove(project);
	}
	
	public void removeAllLeaders() {
		Iterator<Project> iter = leaders.iterator();
		
		while(iter.hasNext()) {
			iter.remove();
			iter.next();
		}
	}
	@Override
	public boolean equals(Object obj) {
		Employee empl = (Employee) obj;

		return firstName.equals(empl.firstName) &&
			   lastName.equals(empl.lastName) &&
			   dateOfBirth.equals(empl.dateOfBirth);
	}
	
	@Override
	public int hashCode() {
		if (firstName != null && lastName != null && dateOfBirth != null)
			return firstName.hashCode() + lastName.hashCode() + dateOfBirth.hashCode();
		
		return super.hashCode();
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%d: %s, %s (%4$td.%4$tm.%4$tY)", id, lastName, firstName, dateOfBirth));
		if (address != null)
			sb.append(", " + address);

		return sb.toString();
	}
}