package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Module implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;	
	private Set<LogbookEntry> logbooks = new HashSet<>();
	private Project projectId;
	
	public Module() {
	}
	
	public Module(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<LogbookEntry> getLogbooks() {
		return logbooks;
	}

	public void setLogbooks(Set<LogbookEntry> logbooks) {
		this.logbooks = logbooks;
	}
	
	public void addLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot add NULL logbook");
		}
		
		logbook.attachModule(this);
		logbooks.add(logbook);
	}
	
	public void removeLogbook(LogbookEntry logbook) {
		if (logbook == null) {
			throw new IllegalArgumentException("Cannot remove NULL logbook");
		}
		
		logbook.detachModule();
		logbooks.remove(logbook);
	}

	public Project getProjectId() {
		return projectId;
	}

	public void setProjectId(Project projectId) {
		attachProject(projectId);
	}
	
	public void attachProject(Project project) {
		if (this.projectId != null) {
			this.projectId.getModules().remove(this);
		}
			
		if (project != null) {
			project.getModules().add(this);
		}
		
		this.projectId = project;
	}
	
	public void detachProject() {
		if (this.projectId != null) {
			this.projectId.getModules().remove(this);
		}
		
		this.projectId = null;
	}

}
